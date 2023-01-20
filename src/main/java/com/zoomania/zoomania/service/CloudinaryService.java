package com.zoomania.zoomania.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.zoomania.zoomania.model.entity.ImageEntity;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.Random;

@Service
public class CloudinaryService {
    private final String CLOUD_NAME = "dnlkkivap";
    private final String API_KEY = "262551365886495";
    private final String API_SECRET = "BH1S1XzSneAQU-x0VnPO3FCsRNo";

    private final String IMAGE_FOLDER = "src\\main\\resources\\static\\images\\";
    private final Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", CLOUD_NAME,
            "api_key", API_KEY,
            "api_secret", API_SECRET,
            "secure", true));


    public ImageEntity uploadPhoto(MultipartFile photo) throws IOException {
        uploadPhotoToServer(photo);

        Map uploadResult = cloudinary.uploader()
                .upload(new File(IMAGE_FOLDER + photo.getOriginalFilename()), ObjectUtils.emptyMap());

        Object url = uploadResult.get("url");
        Object publicId = uploadResult.get("public_id");

        ImageEntity imageEntity = new ImageEntity()
                .setImageUrl(url.toString())
                .setPublicId(publicId.toString());

        deletePhotoFromServer(photo.getOriginalFilename());

        return imageEntity;
    }

    public void deletePhoto(ImageEntity imageEntity) throws IOException {
        cloudinary.uploader().destroy(imageEntity.getPublicId(),ObjectUtils.emptyMap());
    }

    private void deletePhotoFromServer(String fileName) throws IOException {
        File file = new File(IMAGE_FOLDER + fileName);
        FileUtils.forceDelete(file);
    }

    private void uploadPhotoToServer(MultipartFile photo) throws IOException {
        Files.copy(photo.getInputStream(),
                Paths.get(IMAGE_FOLDER + File.separator + photo.getOriginalFilename()),
                StandardCopyOption.REPLACE_EXISTING);
    }
}
