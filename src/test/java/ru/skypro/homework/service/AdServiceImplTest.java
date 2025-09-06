package ru.skypro.homework.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.responseDto.AdDto;
import ru.skypro.homework.service.impl.AdServiceImpl;
import ru.skypro.homework.service.impl.ImageService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

//Unit-тесты для сервисов
@ExtendWith(MockitoExtension.class)
class AdServiceImplTest {

    @Mock
    private AdRepository adRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ImageService imageService;

    @InjectMocks
    private AdServiceImpl adService;

    @Test
    void createAdFromMultipart() throws Exception {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("user@mail.com");

        User user = new User();
        user.setId(1L);
        user.setUsername("user@mail.com");

        when(userRepository.findByUsername("user@mail.com")).thenReturn(Optional.of(user));

        CreateOrUpdateAd dto = new CreateOrUpdateAd();
        dto.setTitle("Test");
        dto.setPrice(1000);
        dto.setDescription("Test Description");

        MultipartFile image = mock(MultipartFile.class);
        when(image.isEmpty()).thenReturn(false);
        when(imageService.saveImage(any(), eq("ads"))).thenReturn("test.jpg");

        Ad savedAd = new Ad();
        savedAd.setId(100L);
        savedAd.setAuthor(user);
        savedAd.setTitle("Test");
        savedAd.setPrice(1000);
        savedAd.setDescription("Test Description");
        savedAd.setImage("/images/ads/test.jpg");

        when(adRepository.save(any(Ad.class))).thenReturn(savedAd);

        AdDto result = adService.createAdFromMultipart(userDetails, dto, image);

        assertNotNull(result);
        assertEquals(100, result.getPk());
        assertEquals(1, result.getAuthor());
        assertEquals("Test", result.getTitle());
        assertEquals(1000, result.getPrice());
        assertEquals("/images/ads/test.jpg", result.getImage());

        verify(adRepository, times(1)).save(any(Ad.class));
        verify(imageService, times(1)).saveImage(any(), eq("ads"));
    }
}
