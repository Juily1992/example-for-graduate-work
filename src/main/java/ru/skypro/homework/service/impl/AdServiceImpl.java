package ru.skypro.homework.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.responseDto.AdDto;
import ru.skypro.homework.responseDto.AdsResponse;
import ru.skypro.homework.responseDto.CommentDto;
import ru.skypro.homework.responseDto.ExtendedAdDto;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.dto.CreateOrUpdateAd;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdServiceImpl implements AdService {

    private final AdRepository adRepository;
    private final ImageService imageService;
    private final UserRepository userRepository;
    private static final Logger log = LoggerFactory.getLogger(AdServiceImpl.class);

    @Override
    public AdDto createAdFromMultipart(UserDetails userDetails, CreateOrUpdateAd dto, MultipartFile image) {
        if (userDetails == null) {
            log.warn("Попытка создания объявления без аутентификации");
            throw new AccessDeniedException("User not authenticated");
        }

        log.info("Начало создания объявления для пользователя: {}", userDetails.getUsername());
        log.debug("DTO: {}", dto);
        log.debug("Изображение: {} ({} bytes)", image.getOriginalFilename(), image.getSize());

        if (image.isEmpty()) {
            log.warn("Получен пустой файл изображения");
            throw new IllegalArgumentException("Image file is empty");
        }

        User dbUser = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> {
                    log.error("Пользователь не найден в БД: {}", userDetails.getUsername());
                    return new EntityNotFoundException("User not found");
                });

        Ad ad = new Ad();
        ad.setTitle(dto.getTitle());
        ad.setPrice(dto.getPrice());
        ad.setDescription(dto.getDescription());
        ad.setAuthor(dbUser);
        ad.setCreatedAt(LocalDateTime.now());

        // Сохраняем изображение
        String filename = imageService.saveImage(image, "ads");
        ad.setImage("/images/ads/" + filename);

        Ad saved = adRepository.save(ad);
        AdDto result = convertToAdDto(saved);

        log.info("Объявление успешно создано, ID: {}", result.getPk());
        return result;

    }

    @Override
    public ExtendedAdDto getExtendedAd(Long id) {
        // TODO: реализовать
        return null;
    }

    @Override
    public AdDto updateAd(Long id, CreateOrUpdateAd dto) {
        // TODO: реализовать
        return null;
    }

    @Override
    public void deleteAd(Long id) {
        // TODO: реализовать
    }

    @Override
    public String updateAdImage(Long id, MultipartFile image) {
        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ad not found"));
        String filename = imageService.saveImage(image, "ads");
        String imageUrl = "/images/ads/" + filename;
        ad.setImage(imageUrl);
        adRepository.save(ad);

        return imageUrl;
    }

    @Override
    public AdsResponse getMyAds(User user) {
        List<Ad> ads = adRepository.findByAuthor(user);
        List<AdDto> dtos = ads.stream()
                .map(this::convertToAdDto)
                .collect(Collectors.toList());
        return new AdsResponse(dtos.size(), dtos);
    }

    private AdDto convertToAdDto(Ad ad) {
        AdDto dto = new AdDto();
        dto.setPk(Math.toIntExact(ad.getId()));
        dto.setAuthor(Math.toIntExact(ad.getAuthor().getId()));
        dto.setTitle(ad.getTitle());
        dto.setPrice(ad.getPrice());
        dto.setImage(ad.getImage()); // например: "/images/ads/1.jpg"
        return dto;
    }

    @Override
    public AdsResponse getAllAds() {
        List<Ad> ads = adRepository.findAll();
        List<AdDto> dtos = ads.stream()
                .map(this::convertToAdDto)
                .collect(Collectors.toList());
        return new AdsResponse(dtos.size(), dtos);
    }
}