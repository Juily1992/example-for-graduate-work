package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.responseDto.AdDto;
import ru.skypro.homework.responseDto.ExtendedAdDto;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.responseDto.AdDto;
import ru.skypro.homework.responseDto.ExtendedAdDto;

/**
 * Маппер для преобразования сущности Ad в DTO.
 */
@Mapper
public interface AdMapper {

    // Экземпляр маппера для использования без внедрения через Spring
    AdMapper INSTANCE = Mappers.getMapper(AdMapper.class);

    /**
     * Преобразует сущность Ad в AdDto.
     *
     * @param ad сущность объявления
     * @return AdDto
     */
    @Mapping(source = "id", target = "pk")
    @Mapping(source = "author.id", target = "author")
    AdDto toAdDto(Ad ad);

    /**
     * Преобразует сущность Ad в ExtendedAdDto.
     * Маппит поля автора: имя, фамилия, email, телефон.
     *
     * @param ad сущность объявления
     * @return ExtendedAdDto
     */
    @Mapping(source = "id", target = "pk")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "image", target = "image")
    // Поля автора
    @Mapping(source = "author.firstName", target = "authorFirstName")
    @Mapping(source = "author.lastName", target = "authorLastName")
    @Mapping(source = "author.username", target = "email")
    @Mapping(source = "author.phone", target = "phone")
    ExtendedAdDto toExtendedAdDto(Ad ad);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "image", ignore = true)
    Ad toAd(CreateOrUpdateAd dto);

    /**
     * Преобразует список объявлений в список AdDto.
     *
     * @param ads список сущностей
     * @return список DTO
     */
    java.util.List<AdDto> toAdDtoList(java.util.List<Ad> ads);

    /**
     * Преобразует список объявлений в список ExtendedAdDto.
     *
     * @param ads список сущностей
     * @return список DTO
     */
    java.util.List<ExtendedAdDto> toExtendedAdDtoList(java.util.List<Ad> ads);
}