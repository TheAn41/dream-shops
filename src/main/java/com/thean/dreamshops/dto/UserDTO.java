package com.thean.dreamshops.dto;

import com.thean.dreamshops.model.Cart;
import com.thean.dreamshops.model.Order;
import com.thean.dreamshops.model.Role;
import lombok.Data;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Data
public class UserDTO implements Serializable {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private List<OrderDTO> orders;
    private CartDTO cart;
}
