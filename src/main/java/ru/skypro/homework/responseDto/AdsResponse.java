package ru.skypro.homework.responseDto;

// как будет выглядеть JSON-ответ при запросах к эндпоинтам, которые возвращают список объявлений с пагинацией, например:
//GET /ads — все объявления
//GET /ads/me — мои объявления

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AdsResponse {
    private Integer count;
    private List<AdDto> results;
}
