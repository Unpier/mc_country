package socialweb.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import socialweb.model.City;
import socialweb.repositories.CityRepository;

import java.io.File;
import java.io.IOException;

@Service
public class CityService {

    private final String id = "id";
    private final String name = "name";
    private final String areas = "areas";

    @Autowired
    private CityRepository cityRepository;



    public void saveCitiesFromJsonFile(String filePathCity) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(new File(filePathCity));
            parseJsonNode(rootNode, null, null, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseJsonNode(JsonNode node, Long parentCountryId, Long parentRegionId, String parentCountryName) {
        if (node.isArray()) {
            node.forEach(subNode -> {
                Long newCountryId = subNode.get(id).asLong();
                String newCountryName = subNode.get(name).asText();
                parseJsonNode(subNode, newCountryId, null, newCountryName);
            });
        } else {
            Long cityId = node.get(id).asLong();
            String cityName = node.get(name).asText();
            cityRepository.save(new City(null, parentCountryId, parentRegionId, parentCountryName, null, cityId, cityName));

            JsonNode areasNode = node.get(areas);
            if (areasNode != null && areasNode.isArray()) {
                areasNode.forEach(areaNode -> {
                    Long newRegionId = areaNode.get(id).asLong();
                    String regionName = areaNode.get(name).asText();
                    parseJsonNode(areaNode, parentCountryId, newRegionId, regionName);
                });
            }
        }
    }
}