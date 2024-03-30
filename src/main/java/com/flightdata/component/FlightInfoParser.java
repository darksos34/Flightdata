package com.flightdata.component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flightdata.errorhandling.FlightErrorHandling;
import com.flightdata.flight.dto.FlightInfoDTO;
import com.flightdata.flight.service.NominatimApiService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Log4j2
@AllArgsConstructor
public class FlightInfoParser {

    private static final int MAX_PROCESSING_TIME_SECONDS = 60;
    private static final int INTERVAL_SECONDS = 10;

    private final NominatimApiService nominatimApiService;

    public FlightInfoDTO parseFlightInfo(String json) {
        long startTime = System.currentTimeMillis();
        long currentTime;
        do {
            try {
                return processFlightInfo(json);
            } catch (IOException | FlightErrorHandling.NotFoundFlightsException |
                     FlightErrorHandling.FailedToRetrievalException e) {
                handleException(e);
            }
            sleepForInterval();
            currentTime = System.currentTimeMillis();
        } while (currentTime - startTime < MAX_PROCESSING_TIME_SECONDS * 1000);

        throw new FlightErrorHandling.MaxProcessingTimeoutException("Max processing time exceeded");
    }

    private FlightInfoDTO processFlightInfo(String json) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(json);
        JsonNode statesNode = rootNode.path("states");

        if (statesNode.isArray() && !statesNode.isEmpty()) {
            JsonNode stateNode = statesNode.get(0);
            FlightInfoDTO flightInfoDTO = new FlightInfoDTO(stateNode);

            String locationJson = nominatimApiService.getCountryLocation(stateNode.get(6).asText(), stateNode.get(5).asText());
            if (locationJson == null) {
                throw new FlightErrorHandling.FailedToRetrievalException("Failed to retrieve country location from JSON");
            }
            flightInfoDTO.setOriginCountry(nominatimApiService.getLocationFromJson(locationJson));

            getFlightInfoFromDTO(flightInfoDTO, stateNode);
            return flightInfoDTO;
        } else {
            throw new FlightErrorHandling.NotFoundFlightsException("No flight states found in JSON");
        }
    }

    private static void handleException(Exception e) {
       log.error("Error processing flight info: " + e.getMessage());
    }

    private static void sleepForInterval() {
        try {
            Thread.sleep(INTERVAL_SECONDS * 1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Thread interrupted while sleeping: " + e.getMessage());
        }
    }

    private static void getFlightInfoFromDTO(FlightInfoDTO flightInfoDTO, JsonNode stateNode) {
        flightInfoDTO.setIcao24(stateNode.get(0).asText());
        flightInfoDTO.setCallsign(stateNode.get(1).asText());
        flightInfoDTO.setOriginCountry(stateNode.get(2).asText());
        flightInfoDTO.setLongitude(stateNode.get(5).asDouble());
        flightInfoDTO.setLatitude(stateNode.get(6).asDouble());
        double velocityMs = stateNode.get(9).asDouble();
        flightInfoDTO.setVelocityKmh(velocityMs * 3.6);
    }
}

