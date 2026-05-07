package com.ecommerce.service;

import com.ecommerce.entity.Banner;
import com.ecommerce.repository.BannerRepository;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;
import java.util.UUID;

@Service
public class BannerService {

    @Autowired
    private MinioClient minioClient;

    @Autowired
    private BannerRepository bannerRepository;

    @Value("${app.minio.bucket.banners:banner-images}")
    private String bucketName;

    @Value("${app.minio.endpoint:http://localhost:9000}")
    private String minioEndpoint;

    public Banner createBanner(String title, String redirectUrl, String type, MultipartFile file) throws Exception {
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        try (InputStream is = file.getInputStream()) {
            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .stream(is, file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build()
            );
        }

        String imageUrl = minioEndpoint + "/" + bucketName + "/" + fileName;
        Banner banner = new Banner();
        banner.setImageUrl(imageUrl);
        return bannerRepository.save(banner);
    }
}
