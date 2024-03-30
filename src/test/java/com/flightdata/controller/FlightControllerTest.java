package com.flightdata.controller;

import com.flightdata.flight.controller.FlightController;
import com.flightdata.flight.dto.FlightInfoDTO;
import com.flightdata.flight.service.OpenSkyApiService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(FlightController.class)
class FlightControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OpenSkyApiService openSkyApiService;

    @Test
    void getFlightInfo() throws Exception {
        when(openSkyApiService.getFlightInfo(anyString())).thenReturn(createFlightInfo());

        mockMvc.perform(get("/flights/flight-info")
                        .param("transponderAddress", anyString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())

                .andExpect(jsonPath("$.callsign", Matchers.is("FLIGHT123")))
                .andExpect(jsonPath("$.icao24", Matchers.is("ABC123")))
                .andExpect(jsonPath("$.originCountry", Matchers.is("NL")))
                .andExpect(jsonPath("$.longtitude", Matchers.is(80.000)))
                .andExpect(jsonPath("$.latitude", Matchers.is(35.0000)))
                .andExpect(jsonPath("$.velocityKmh", Matchers.is(800.0)));

    }

    private FlightInfoDTO createFlightInfo() {
     return   FlightInfoDTO.builder()
        .icao24("ABC123")
        .callsign("FLIGHT123")
        .originCountry("NL")
        .longtitude(80.000)
        .latitude(35.0000)
        .velocityKmh(800.0)
        .build();
    }
}
