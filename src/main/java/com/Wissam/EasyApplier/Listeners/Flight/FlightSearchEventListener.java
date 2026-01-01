package com.Wissam.EasyApplier.Listeners.Flight;

import java.util.List;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.Wissam.EasyApplier.Events.Flight.FlightSearchEvent;
import com.Wissam.EasyApplier.Extractions.Jsoup.FlightsInfoGatherer;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FlightSearchEventListener {

  private final FlightsInfoGatherer gatherer;
  private final SimpMessagingTemplate messagingTemplate;

  @Async
  @EventListener
  public void onFlightSearchEvent(FlightSearchEvent event) {
    try {
      var results = gatherer.findFlights(event.flightInfo());
      messagingTemplate.convertAndSend("/topic/flights/" + event.searchId(), results);
    } catch (Exception e) {
      e.printStackTrace();
      messagingTemplate.convertAndSend("/topic/flights/" + event.searchId(),
          List.of("Error: " + e.getMessage()));
    }
  }

}
