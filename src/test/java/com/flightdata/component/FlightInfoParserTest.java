package com.flightdata.component;

import com.flightdata.errorhandling.FlightErrorHandling;
import com.flightdata.flight.service.NominatimApiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

class FlightInfoParserTest {

    @Mock
    private NominatimApiService nominatimApiService;

    @InjectMocks
    private FlightInfoParser flightInfoParser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testParseFlightInfo_NullInput() {
        assertThrows(IllegalArgumentException.class, () -> flightInfoParser.parseFlightInfo(null));
    }

    @Test
    void testParseFlightInfo_MalformedJSON() {
        String json = "{\"states\":[";

        assertThrows(FlightErrorHandling.MaxProcessingTimeoutException.class, () -> flightInfoParser.parseFlightInfo(json));
    }

    @Test
    void testParseFlightInfo_NominatimApiServiceFailure(){
        String json = "{\"states\":[[\"icao24\", \"callsign\", \"originCountry\", \"...\"]]}";

        when(nominatimApiService.getCountryLocation(anyString(), anyString())).thenThrow(NullPointerException.class);

        assertThrows(NullPointerException.class, () -> flightInfoParser.parseFlightInfo(json));
    }

}
