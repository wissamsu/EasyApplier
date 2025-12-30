package com.Wissam.EasyApplier.Events.Flight;

import com.Wissam.EasyApplier.Extractions.Jsoup.Models.FlightInfo;

public record FlightSearchEvent(FlightInfo flightInfo, String searchId) {
}
