package com.ecommerce.service;

import com.ecommerce.entity.Wishlist;
import com.ecommerce.entity.WishlistItem;
import com.ecommerce.entity.Product;
import com.ecommerce.entity.User;
import com.ecommerce.repository.WishlistRepository;
import com.ecommerce.repository.WishlistItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
public class WishlistService {

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private WishlistItemRepository wishlistItemRepository;

    public Optional<Wishlist> getWishlistByUser(User user) {
        return wishlistRepository.findByUserId(user.getId());
    }

    @Transactional
    public Wishlist addProductToWishlist(User user, Product product) {
        Wishlist wishlist = wishlistRepository.findByUserId(user.getId()).orElseGet(() -> {
            Wishlist newWishlist = new Wishlist();
            newWishlist.setUser(user);
            return wishlistRepository.save(newWishlist);
        });

        boolean itemExists = wishlist.getItems().stream()
                .anyMatch(item -> item.getProduct().getId().equals(product.getId()));

        if (!itemExists) {
            WishlistItem item = new WishlistItem();
            item.setWishlist(wishlist);
            item.setProduct(product);
            wishlist.getItems().add(item);
            wishlistItemRepository.save(item);
        }
        return wishlistRepository.save(wishlist);
    }
}
