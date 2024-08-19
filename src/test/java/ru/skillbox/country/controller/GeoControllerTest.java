package ru.skillbox.country.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.skillbox.country.controller.GeoController;
import ru.skillbox.country.dto.CityDto;
import ru.skillbox.country.dto.CountryDto;
import ru.skillbox.country.service.GeoService;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(GeoController.class)
public class GeoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GeoService service;

    @Test
    public void testGetCountry() throws Exception {
        CountryDto country = new CountryDto();
        country.setId(1L);
        country.setTitle("Russia");

        when(service.getCountries()).thenReturn(Collections.singletonList(country));

        mockMvc.perform(get("/api/v1/geo/country"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Russia"));
    }

    @Test
    public void testGetCity() throws Exception {
        CityDto city = new CityDto();
        city.setId(1L);
        city.setTitle("Moscow");
        city.setCountryId(1L);

        when(service.getCities(1L)).thenReturn(Collections.singletonList(city));

        mockMvc.perform(get("/api/v1/geo/country/1/city"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Moscow"));
    }
}