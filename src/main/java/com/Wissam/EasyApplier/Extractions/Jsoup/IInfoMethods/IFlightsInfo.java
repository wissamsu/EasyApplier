package com.Wissam.EasyApplier.Extractions.Jsoup.IInfoMethods;

import java.util.List;

import com.Wissam.EasyApplier.Extractions.Jsoup.Models.FlightInfo;
import com.Wissam.EasyApplier.Extractions.Jsoup.Models.FlightsFound;

public interface IFlightsInfo {

  List<FlightsFound> findFlights(FlightInfo flightInfo);

}
