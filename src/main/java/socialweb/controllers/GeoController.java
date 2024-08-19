package socialweb.controllers;

import socialweb.dto.CityDto;
import socialweb.dto.CountryDto;
import socialweb.services.GeoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/geo")
public class GeoController {

    private final GeoService service;

    @GetMapping("/country")
    public List<CountryDto> getCountry() {
        return service.getCountries();
    }

    @GetMapping("/country/{id}/city")
    public List<CityDto> getCity(@PathVariable Long id) {
        return service.getCities(id);
    }
}
