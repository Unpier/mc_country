package ru.skillbox.country.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.skillbox.country.dto.CityDto;
import ru.skillbox.country.dto.CountryDto;
import ru.skillbox.country.entity.Geo;
import ru.skillbox.country.mapper.GeoMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class GeoServiceTest {

    @InjectMocks
    private GeoService geoService;

    @Mock
    private GeoMapper geoMapper;

    private Map<Long, Geo> geos;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ObjectMapper mapper = new ObjectMapper();
        try {
            String path = getPathToCitiesJson();
            File jsonFile = new File(path);
            Set<Geo> geoSet = mapper.readValue(jsonFile, new TypeReference<Set<Geo>>() {});
            geos = geoSet.stream().collect(Collectors.toMap(Geo::getId, geo -> geo));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getPathToCitiesJson() {
        String path = "C:\\prog\\Prog\\mc_country10\\geo\\cities";
        return path;
    }


    @Test
    public void testGetCountries() {
        List<CountryDto> expectedCountries = Arrays.asList(
                new CountryDto(1L, "Country1"),
                new CountryDto(2L, "Country2")
        );

        when(geoMapper.toCountryDtoList((List<Geo>) geos.values())).thenReturn(expectedCountries);

        List<CountryDto> actualCountries = geoService.getCountries();

        assertEquals(expectedCountries.size(), actualCountries.size());
        for (int i = 0; i < expectedCountries.size(); i++) {
            assertEquals(expectedCountries.get(i).getId(), actualCountries.get(i).getId());
            assertEquals(expectedCountries.get(i).getTitle(), actualCountries.get(i).getTitle());
        }
    }

    @Test
    public void testGetCities() {
        Long countryId = 1L;
        List<CityDto> expectedCities = Arrays.asList(
                new CityDto(1L, "City1", countryId),
                new CityDto(2L, "City2", countryId)
        );

        when(geoMapper.toCityDtoList(geos.get(countryId).getAreas())).thenReturn(expectedCities);

        List<CityDto> actualCities = geoService.getCities(countryId);

        assertEquals(expectedCities.size(), actualCities.size());
        for (int i = 0; i < expectedCities.size(); i++) {
            assertEquals(expectedCities.get(i).getId(), actualCities.get(i).getId());
            assertEquals(expectedCities.get(i).getTitle(), actualCities.get(i).getTitle());
            assertEquals(expectedCities.get(i).getCountryId(), actualCities.get(i).getCountryId());
        }
    }
}
