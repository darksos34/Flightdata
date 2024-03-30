package com.flightdata.flight.controller;

import com.flightdata.flight.dto.FlightInfoDTO;
import com.flightdata.flight.service.OpenSkyApiService;
import com.flightdata.request.RequestPath;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(RequestPath.FLIGHT)
@RequiredArgsConstructor
public class FlightController {

    private final OpenSkyApiService openSkyApiService;

    @GetMapping("/flight-info")
    public ResponseEntity<FlightInfoDTO> getFlightInfo(@RequestParam String transponderAddress) {
        return ResponseEntity.ok(openSkyApiService.getFlightInfo(transponderAddress));
    }
}
