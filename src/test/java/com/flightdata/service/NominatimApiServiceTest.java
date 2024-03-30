package com.flightdata.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flightdata.flight.service.NominatimApiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NominatimApiServiceTest {

    @MockBean
    private NominatimApiService nominatimApiService;

    @MockBean
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        restTemplate = mock(RestTemplate.class);
        nominatimApiService = new NominatimApiService(restTemplate);
    }
    @Test
    void testGetCountryLocation() {
        String mockResponse = "{\"country\":\"Netherlands\"}";
        String latitude = "52.370216";
        String longitude = "4.895168";

        when(restTemplate.getForObject(
                "https://nominatim.openstreetmap.org/reverse?lat=52.370216&lon=4.895168&format=json",
                String.class))
                .thenReturn(mockResponse);

        String result = nominatimApiService.getCountryLocation(latitude, longitude);

        assertEquals("{\"country\":\"Netherlands\"}", result);
    }

    @Test
    void testGetLocationFromJson() throws IOException {
        String json = "{\"display_name\":\"Berlin, Germany\"}";

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(json);

        String expectedLocation = rootNode.get("display_name").asText();
        String location = nominatimApiService.getLocationFromJson(json);

        assertEquals(expectedLocation, location);
    }
}
