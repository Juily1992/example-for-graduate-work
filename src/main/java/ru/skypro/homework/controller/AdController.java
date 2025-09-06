package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.responseDto.AdDto;
import ru.skypro.homework.responseDto.AdsResponse;
import ru.skypro.homework.responseDto.ExtendedAdDto;
import ru.skypro.homework.service.AdService;

@RestController
@Transactional
@RequestMapping("/ads")
@Tag(name = "Ads", description = "API для работы с объявлениями: создание, просмотр, редактирование, удаление")
@RequiredArgsConstructor

public class AdController {

    private final AdService adService;
    private final UserRepository userRepository;

    @Operation(
            summary = "Получение всех объявлений",
            description = "Возвращает список всех объявлений с пагинацией."
    )
    @GetMapping
    public ResponseEntity<AdsResponse> getAllAds() {
        return ResponseEntity.ok(adService.getAllAds());
    }

    @Operation(
            summary = "Получение своих объявлений",
            description = "Возвращает все объявления текущего пользователя."
    )
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AdsResponse> getMyAds(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        AdsResponse response = adService.getMyAds(userDetails.getUsername());
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Создание объявления",
            description = "Создаёт новое объявление с заголовком, ценой, описанием и изображением.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(type = "object"),
                            encoding = {
                                    @Encoding(name = "properties", contentType = "application/json"),
                                    @Encoding(name = "image", contentType = "image/jpeg, image/png")
                            }
                    )
            )
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AdDto> addAd(
            @RequestPart("properties") @Valid CreateOrUpdateAd adDto,
            @RequestPart("image") MultipartFile image,
            @AuthenticationPrincipal UserDetails userDetails) {

        AdDto ad = adService.createAdFromMultipart(userDetails, adDto, image);
        return ResponseEntity.status(HttpStatus.CREATED).body(ad);
    }

    @Operation(
            summary = "Полная информация об объявлении",
            description = "Возвращает расширенные данные объявления: автор, описание, изображение и т.д."
    )
    @GetMapping("/{id}")
    @PreAuthorize("@adServiceImpl.isOwner(#id, authentication.principal.username) or hasRole('ADMIN')")
    public ResponseEntity<ExtendedAdDto> getAd(@PathVariable Long id) {
        ExtendedAdDto dto = adService.getExtendedAd(id);
        return ResponseEntity.ok(dto);
    }

    @Operation(
            summary = "Удаление объявления",
            description = "Удаляет объявление по ID. Только автор или администратор может удалить."
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("@adServiceImpl.isOwner(#id, authentication.principal.username) or hasRole('ADMIN')")
    public ResponseEntity<Void> removeAd(@PathVariable Long id) {
        adService.deleteAd(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Редактирование объявления",
            description = "Изменяет заголовок и цену объявления. Автор остаётся прежним."
    )
    @PatchMapping("/{id}")
    @PreAuthorize("@adServiceImpl.isOwner(#id, authentication.principal.username) or hasRole('ADMIN')")
    public ResponseEntity<AdDto> updateAd(
            @PathVariable Long id,
            @RequestBody @Valid CreateOrUpdateAd dto) {
        AdDto updated = adService.updateAd(id, dto);
        return ResponseEntity.ok(updated);
    }

    @Operation(
            summary = "Обновление изображения объявления",
            description = "Заменяет изображение у существующего объявления."
    )
    @PatchMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("@adServiceImpl.isOwner(#id, authentication.principal.username) or hasRole('ADMIN')")
    public ResponseEntity<String> updateAdImage(
            @PathVariable Long id,
            @RequestPart("image") MultipartFile image) {
        String imageUrl = adService.updateAdImage(id, image);
        return ResponseEntity.ok(imageUrl);
    }
}