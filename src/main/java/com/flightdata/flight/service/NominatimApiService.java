package com.flightdata.flight.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class NominatimApiService {

    private static final String NOMINATIM_API_URL = "https://nominatim.openstreetmap.org/reverse";
    private final RestTemplate restTemplate;

    public String getCountryLocation(String latitude, String longitude) {
        String apiUrl = String.format("%s?lat=%s&lon=%s&format=json", NOMINATIM_API_URL, latitude, longitude);
        return restTemplate.getForObject(apiUrl, String.class);
    }

    public String getLocationFromJson(String json) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(json);
        return rootNode.get("display_name").asText();
    }
}
