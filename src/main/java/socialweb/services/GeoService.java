package socialweb.services;
;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import socialweb.dto.CityDto;
import socialweb.dto.CountryDto;
import socialweb.entity.Geo;
import socialweb.exception.NotFoundException;
import socialweb.mapper.GeoMapper;

import java.io.IOException;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
public class GeoService {

    private final GeoMapper geoMapper;
    private ObjectMapper mapper = new ObjectMapper();
    private List<Geo> geos;

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
                    new TypeReference<>() {}
            );
        } catch (IOException e) {
            log.warn(e.toString());
        }
    }

    public List<CountryDto> getCountries() {
        return geoMapper.toCountryDtoList(geos);
    }

    public List<CityDto> getCities(Long countryId) {
        return geoMapper.toCityDtoList(
                geos.stream()
                        .filter(geo -> geo.getId().equals(countryId))
                        .findFirst()
                        .orElseThrow(() ->
                                new NotFoundException(
                                        MessageFormat.format("Страна с id {0} не найдена", countryId)
                                )
                        )
                        .getAreas()
                        .stream()
                        .flatMap(geo -> geo.getAreas() != null ? geo.getAreas().stream() : Stream.of(geo))
                        .toList()
        );
    }
}
