package ru.skillbox.country.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.skillbox.country.dto.CityDto;
import ru.skillbox.country.dto.CountryDto;
import ru.skillbox.country.entity.Geo;
import ru.skillbox.country.mapper.GeoMapper;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
public class GeoService {

    private final GeoMapper geoMapper;
    private final ObjectMapper mapper = new ObjectMapper();
    Map<Long, Geo> geos;

    {
        try {
            String path = System.getProperty("java.class.path");
            String slash = "/";
            if (System.getProperty("os.name").startsWith("Windows")) {
                slash = "\\";
            }
            path = path.substring(0, path.indexOf(slash, path.indexOf("country"))) + "/geo/cities.json";

            log.info(path);

            geos = mapper.readValue(
                            Paths.get(path).toFile(),
                            new TypeReference<Set<Geo>>() {}
                    )
                    .stream()
                    .peek(geo -> geo
                            .setAreas(
                                    geo.getAreas()
                                            .stream()
                                            .flatMap(g ->
                                                    g.getAreas().isEmpty() ?
                                                            Stream.of(g) : g.getAreas().stream()
                                            )
                                            .sorted(Geo::compareTo)
                                            .toList()
                            )
                    )
                    .collect(
                            Collectors.toMap(
                                    Geo::getId,
                                    geo -> geo
                            )
                    );
        } catch (IOException e) {
            log.warn(e.toString());
        }
    }

    public List<CountryDto> getCountries() {
        return geoMapper.toCountryDtoList(
                geos.values().stream().sorted(Geo::compareTo).toList()
        );
    }

    public List<CityDto> getCities(Long countryId) {
        return geoMapper.toCityDtoList(geos.get(countryId).getAreas());
    }
}
