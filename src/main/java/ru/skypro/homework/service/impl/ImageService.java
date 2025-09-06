package ru.skypro.homework.service.impl;

import jakarta.annotation.PostConstruct;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ImageService {

    private final Path rootLocation = Paths.get("src/main/resources/images").toAbsolutePath().normalize();

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload folder!");
        }
    }

    public String saveImage(MultipartFile file, String folder) {
        try {
            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path destination = rootLocation.resolve(folder).resolve(filename).normalize();
            Files.createDirectories(destination.getParent());
            Files.copy(file.getInputStream(), destination);
            return filename; //  возвращаем имя
        } catch (IOException e) {
            throw new RuntimeException("Failed to save image", e);
        }
    }

    public Resource loadAsResource(String filename, String folder) {
        try {
            Path file = rootLocation.resolve(folder).resolve(filename).normalize();

            if (!Files.exists(file)) {
                throw new RuntimeException("File not found: " + filename);
            }
            if (!Files.isReadable(file)) {
                throw new RuntimeException("File is not readable: " + filename);
            }

            return new UrlResource(file.toUri());

        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid URL: " + filename, e);
        }
    }
}