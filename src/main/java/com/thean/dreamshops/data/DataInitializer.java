//package com.thean.dreamshops.data;
//
//import com.thean.dreamshops.model.User;
//import com.thean.dreamshops.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.context.event.ApplicationReadyEvent;
//import org.springframework.context.ApplicationListener;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {
//    private final UserRepository userRepository;
//
//@Override
//    public void onApplicationEvent(ApplicationReadyEvent event) {
//        createDefaultUsersIfNotExits();
//    }
//
//    private void createDefaultUsersIfNotExits() {
//    for(int i=1;i<=5;i++){
//        String defaultEmail = "user"+i+"@gmail.com";
//        if(userRepository.existsByEmail(defaultEmail)){
//            continue;
//        }
//        User user = new User();
//        user.setEmail(defaultEmail);
//        user.setPassword("password");
//        user.setFirstName("The User");
//        user.setLastName("User"+i);
//        userRepository.save(user);
//        System.out.println("User created: "+user);
//    }
//    }
//}
