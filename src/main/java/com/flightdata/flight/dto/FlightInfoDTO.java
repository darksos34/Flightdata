package com.flightdata.flight.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class FlightInfoDTO {
    private String icao24;
    private String callsign;
    private String originCountry;
    private Double velocityKmh;
    private Double longtitude;
    private Double latitude;

    public FlightInfoDTO(JsonNode stateNode) {
    }

    public FlightInfoDTO() {
    }

}
