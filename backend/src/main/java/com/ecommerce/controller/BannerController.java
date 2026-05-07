package com.ecommerce.controller;

import com.ecommerce.entity.Banner;
import com.ecommerce.service.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/banners")
public class BannerController {

    @Autowired
    private BannerService bannerService;

    @PostMapping("/admin")
    public ResponseEntity<Banner> createBanner(@RequestParam String title, @RequestParam String redirectUrl,
            @RequestParam String type, @RequestParam MultipartFile file) throws Exception {
        return ResponseEntity.ok(bannerService.createBanner(title, redirectUrl, type, file));
    }
}
