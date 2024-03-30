package com.flightdata.flight.service;

import com.flightdata.component.FlightInfoParser;
import com.flightdata.errorhandling.FlightErrorHandling;
import com.flightdata.flight.dto.FlightInfoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class OpenSkyApiService {

    private static final String OPENSKY_API_URL = "https://opensky-network.org/api/states/all";
    private final RestTemplate restTemplate;
    private final FlightInfoParser flightInfoParser;

    public FlightInfoDTO getFlightInfo(String transponderAddress) {
        String apiUrl = String.format("%s?icao24=%s", OPENSKY_API_URL, transponderAddress);
        String json = restTemplate.getForObject(apiUrl, String.class);
        if (json != null) {
            return flightInfoParser.parseFlightInfo(json);
        } else {
            throw new FlightErrorHandling.FailedToRetrievalException("Failed to fetch JSON data from API");
        }
    }

}
