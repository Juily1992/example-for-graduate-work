package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.skypro.homework.service.impl.ImageService;

import java.io.IOException;
import java.nio.file.Files;

@RestController
@Tag(name = "Images", description = "API для отдачи изображений объявлений и пользователей")
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @Operation(
            summary = "Получение изображения объявления",
            description = "Возвращает изображение по имени файла."
    )
    @GetMapping("/images/ads/{filename:.+}")
    public ResponseEntity<Resource> getAdImage(@PathVariable String filename) {
        try {
            Resource file = imageService.loadAsResource(filename, "ads");
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getFilename() + "\"")
                    .body(file);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
            summary = "Получение аватара пользователя",
            description = "Возвращает изображение профиля пользователя."
    )
    @GetMapping("/users/{filename:.+}")
    public ResponseEntity<Resource> getUserImage(@PathVariable String filename) throws IOException {
        Resource resource = new ClassPathResource("images/users/" + filename);
        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }
        String contentType = Files.probeContentType(resource.getFile().toPath());
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }
}