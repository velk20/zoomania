package com.zoomania.zoomania.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
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
    private final Random random = new Random();
    private final String IMAGE_FOLDER = "src\\main\\resources\\static\\images\\";
    private final  Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", "dnlkkivap",
            "api_key", "262551365886495",
            "api_secret", "BH1S1XzSneAQU-x0VnPO3FCsRNo",
            "secure", true));


    public String uploadPhoto(MultipartFile photo) throws IOException {
        uploadPhotoToServer(photo);

        Map uploadResult = cloudinary.uploader()
                .upload(new File(IMAGE_FOLDER+photo.getOriginalFilename()), ObjectUtils.emptyMap());

        Object url = uploadResult.get("url");
        System.out.println(url);

         deletePhotoFromServer(photo.getOriginalFilename());

        return url.toString();
    }

    private void deletePhotoFromServer(String fileName) throws IOException {
        File file = new File(IMAGE_FOLDER + fileName);
         FileUtils.forceDelete(file);
    }

    private void uploadPhotoToServer(MultipartFile photo) throws IOException {
        Files.copy(photo.getInputStream(),
                Paths.get(IMAGE_FOLDER+File.separator+photo.getOriginalFilename()),
                StandardCopyOption.REPLACE_EXISTING);
    }
}
