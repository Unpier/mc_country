package ru.skillbox.country.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.skillbox.country.dto.CityDto;
import ru.skillbox.country.dto.CountryDto;
import ru.skillbox.country.entity.Geo;
import ru.skillbox.country.mapper.GeoMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GeoMapperTest {

    private final GeoMapper geoMapper = Mappers.getMapper(GeoMapper.class);

    @Test
    public void testToCountryDto() {
        Geo geo = new Geo();
        geo.setId(1L);
        geo.setName("Россия");

        CountryDto countryDto = geoMapper.toCountryDto(geo);

        assertEquals(1L, countryDto.getId());
        assertEquals("Россия", countryDto.getTitle());
    }

    @Test
    public void testToCityDto() {
        Geo geo = new Geo();
        geo.setId(1L);
        geo.setName("Москва");
        geo.setParentId(1L);

        CityDto cityDto = geoMapper.toCityDto(geo);

        assertEquals(1L, cityDto.getId());
        assertEquals("Москва", cityDto.getTitle());
        assertEquals(1L, cityDto.getCountryId());
    }
}