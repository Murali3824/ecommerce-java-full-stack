package com.ecommerce.controller;

import com.ecommerce.entity.Wishlist;
import com.ecommerce.security.UserPrincipal;
import com.ecommerce.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wishlist")
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    @GetMapping
    public ResponseEntity<Wishlist> getWishlist(@AuthenticationPrincipal UserPrincipal principal) {
        Wishlist wishlist = wishlistService.getWishlistByUser(principal.getUser()).orElseGet(() -> {
            Wishlist newWishlist = new Wishlist();
            newWishlist.setUser(principal.getUser());
            return newWishlist;
        });
        return ResponseEntity.ok(wishlist);
    }
}
