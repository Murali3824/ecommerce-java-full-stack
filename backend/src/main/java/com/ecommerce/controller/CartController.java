package com.ecommerce.controller;

import com.ecommerce.entity.Cart;
import com.ecommerce.entity.User;
import com.ecommerce.security.UserPrincipal;
import com.ecommerce.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping
    public ResponseEntity<Cart> getCart(@AuthenticationPrincipal UserPrincipal principal) {
        User user = principal.getUser();
        Cart cart = cartService.getCartByUser(user).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            return newCart;
        });
        return ResponseEntity.ok(cart);
    }
}
