package ru.skypro.homework.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.model.User;
import ru.skypro.homework.responseDto.AdDto;
import ru.skypro.homework.responseDto.AdsResponse;
import ru.skypro.homework.responseDto.ExtendedAdDto;
import ru.skypro.homework.dto.CreateOrUpdateAd;

public interface AdService {
    AdDto createAdFromMultipart(UserDetails userDetails, CreateOrUpdateAd dto, MultipartFile image);

    ExtendedAdDto getExtendedAd(Long id);

    AdDto updateAd(Long id, CreateOrUpdateAd dto);

    void deleteAd(Long id);

    AdsResponse getMyAds(User user);

    AdsResponse getAllAds();

    String updateAdImage(Long id, MultipartFile image);
}