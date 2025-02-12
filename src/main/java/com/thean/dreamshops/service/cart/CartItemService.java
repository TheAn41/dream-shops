package com.thean.dreamshops.service.cart;

import com.thean.dreamshops.exception.NotFoundException;
import com.thean.dreamshops.model.Cart;
import com.thean.dreamshops.model.CartItem;
import com.thean.dreamshops.model.Product;
import com.thean.dreamshops.repository.CartItemRepository;
import com.thean.dreamshops.repository.CartRepository;
import com.thean.dreamshops.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CartItemService implements ICartItemService {
    private final CartItemRepository cartItemRepository;
    private final IProductService productService;
    private final ICartService cartService;
    private final CartRepository cartRepository;


    @Override
    public void addItemToCart(Long cartId, Long productId, int quantity) {
        //1.get the cart
        //2. get the product
        //3.check if the product already in the cart
        //4.if yes, then create increase the quantity with the request quantity
        //5.if no, the initiate a new cartItem entry

        Cart cart = cartService.getCart(cartId);
        Product product = productService.getProductById(productId);
        CartItem cartItem = cart.getItems()
                .stream().
                filter(item->item.getProduct().getId().equals(productId))
                .findFirst().orElse(new CartItem());

        if (cartItem.getId() == null) {
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setUnitPrice(BigDecimal.valueOf(product.getPrice()));

        }else {
            cartItem.setQuantity(cartItem.getQuantity()+quantity);
        }
        cartItem.setTotalPrice();
        cart.addItem(cartItem);
        cartItemRepository.save(cartItem);
        cartRepository.save(cart);
    }

    @Override
    public void removeItemFromCart(Long cartId, Long productId) {
        Cart cart = cartService.getCart(cartId);
        CartItem itemToRemove = getCartItem(cartId, productId);
        cart.removeItem(itemToRemove);
        cartRepository.save(cart);

    }

    @Override
    public void updateItemQuantity(Long cartId, Long productId, int quantity) {
        Cart cart = cartService.getCart(cartId);
        cart.getItems()
                .stream()
                .filter(item->item.getProduct().getId().equals(productId))
                .findFirst()
                .ifPresent(item ->{
                    item.setQuantity(quantity);
                    item.setUnitPrice(BigDecimal.valueOf(item.getProduct().getPrice()));
                    item.setTotalPrice();
                });

        BigDecimal totalAmount  = cart.getTotalAmount();
        cart.setTotalAmount(totalAmount);
        cartRepository.save(cart);
    }
    @Override
    public CartItem getCartItem(Long cartId, Long productId) {
        Cart cart = cartService.getCart(cartId);
        return cart.getItems()
                .stream()
                .filter(item->item.getProduct().getId().equals(productId))
                .findFirst().orElseThrow(()-> new NotFoundException("Item not found!"));
    }
}
