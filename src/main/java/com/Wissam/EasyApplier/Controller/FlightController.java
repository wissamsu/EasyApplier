package com.Wissam.EasyApplier.Controller;

import java.util.UUID;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Wissam.EasyApplier.Events.Flight.FlightSearchEvent;
import com.Wissam.EasyApplier.Extractions.Jsoup.Models.FlightInfo;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/flight")
@RequiredArgsConstructor
@Tag(name = "Flight", description = "Flight controller")
public class FlightController {

  private final ApplicationEventPublisher publisher;

  @PostMapping("/search")
  public ResponseEntity<String> findFlights(@RequestBody FlightInfo flightInfo) {
    String searchId = UUID.randomUUID().toString();
    publisher.publishEvent(new FlightSearchEvent(flightInfo, searchId));
    return ResponseEntity.accepted().body(searchId);
  }

}
