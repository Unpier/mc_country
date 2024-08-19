package socialweb.services;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import socialweb.model.Country;
import socialweb.repositories.CountryRepository;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;

    public CountryServiceImpl(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    public void saveCountriesFromJsonFile(String filePathCounty) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<Country> countries = Arrays.asList(objectMapper.readValue(new File(filePathCounty), Country[].class));
            countryRepository.saveAll(countries);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
