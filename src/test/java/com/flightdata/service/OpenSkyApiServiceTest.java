package com.flightdata.service;

import com.flightdata.component.FlightInfoParser;
import com.flightdata.flight.dto.FlightInfoDTO;
import com.flightdata.flight.service.OpenSkyApiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OpenSkyApiServiceTest {

    private static final String json = "{\"icao24\":\"ABC123\",\"callsign\":\"FLIGHT123\",\"originCountry\":\"NL\"," +
            "\"longitude\":80.000,\"latitude\":35.0000,\"velocityKmh\":800.0}";

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private FlightInfoParser flightInfoParser;

    @InjectMocks
    private OpenSkyApiService openSkyApiService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this.openSkyApiService);
    }

    @Test
    void testGetFlightInfo() {
        when(restTemplate.getForObject(anyString(), any(Class.class))).thenReturn(json);

        FlightInfoDTO mockFlightInfo = createMockFlightInfo();
        when(flightInfoParser.parseFlightInfo(json)).thenReturn(mockFlightInfo);

        String transponderAddress = "ABC123";
        FlightInfoDTO result = openSkyApiService.getFlightInfo(transponderAddress);

        assertNotNull(result);
        assertEquals(mockFlightInfo, result); // Controleer of de resultaten overeenkomen met het verwachte object
    }

    private FlightInfoDTO createMockFlightInfo() {

        FlightInfoDTO mockFlightInfo = new FlightInfoDTO();
        mockFlightInfo.setIcao24("ABC123");
        mockFlightInfo.setCallsign("FLIGHT123");
        mockFlightInfo.setOriginCountry("NL");
        mockFlightInfo.setLongtitude(80.000);
        mockFlightInfo.setLatitude(35.0000);
        mockFlightInfo.setVelocityKmh(800.0);
        return mockFlightInfo;
    }

    @Test
    void testGetFlightInfoWithApiFailure() {
        when(restTemplate.getForObject(anyString(), any(Class.class))).thenReturn(null);

        String transponderAddress = "ABC123";
        assertThrows(RuntimeException.class, () -> openSkyApiService.getFlightInfo(transponderAddress));
    }

}
