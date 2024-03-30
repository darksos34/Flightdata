package com.flightdata.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flightdata.flight.service.NominatimApiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
class NominatimApiServiceTest {

    private NominatimApiService nominatimApiService;

    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        restTemplate = mock(RestTemplate.class);
        nominatimApiService = new NominatimApiService(restTemplate);
    }

    @Test
    void testGetCountryLocation() {
        // Mocking van de API-response
        String latitude = "52.5200";
        String longitude = "13.4050";
        String expectedJson = "{\"display_name\":\"Berlin, Germany\"}";

        when(restTemplate.getForObject(expectedJson, String.class)).thenReturn(expectedJson);

        // Roep de methode aan om de JSON te krijgen
        String json = nominatimApiService.getCountryLocation(latitude, longitude);

        // Controleer of de juiste JSON is geretourneerd
        assertEquals(expectedJson, json);
    }

    @Test
    void testGetLocationFromJson() throws IOException {
        // Mocking van de JSON-response
        String json = "{\"display_name\":\"Berlin, Germany\"}";
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(json);
        String expectedLocation = rootNode.get("display_name").asText();

        // Roep de methode aan om de locatie te krijgen
        String location = nominatimApiService.getLocationFromJson(json);

        // Controleer of de juiste locatie is geretourneerd
        assertEquals(expectedLocation, location);
    }
}
//
//@ExtendWith(MockitoExtension.class)
//public class NominatimApiServiceTest {
//   private static final String json = "{\"display_name\":\"Berlin, Germany\"}";
//    @InjectMocks
//    private NominatimApiService nominatimApiService;
//
//    @Mock
//    private RestTemplate restTemplate;
//
//    @MockBean
//    private FlightInfoParser flightInfoParser;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this.nominatimApiService);
//    }
//    @org.junit.jupiter.api.Test
//    void testGetCountryLocation() {
//        when(restTemplate.getForObject(anyString(), any(Class.class))).thenReturn(json);
//
//        FlightInfoDTO mockFlightInfo = createMockFlightInfo();
//        when(flightInfoParser.parseFlightInfo(json)).thenReturn(mockFlightInfo);
//        String latitude = String.valueOf(52.5200);
//        String longitude = String.valueOf(13.4050);
//        String result = nominatimApiService.getCountryLocation(latitude,longitude);
//
//       nominatimApiService.getCountryLocation(latitude,longitude);
//
//        assertNotNull(result);
//        assertEquals(mockFlightInfo, json); // Controleer of de resultaten overeenkomen met het verwachte object
//    }
//
//
//    private FlightInfoDTO createMockFlightInfo() {
//        FlightInfoDTO mockFlightInfo = new FlightInfoDTO();
//        mockFlightInfo.setLongtitude(13.4050);
//        mockFlightInfo.setLatitude(52.5200);
//        return mockFlightInfo;
//    }
//
//}
