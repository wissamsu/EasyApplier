package com.Wissam.EasyApplier.Extractions.Jsoup.Models;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FlightInfo {

  private String cityFrom;

  private String cityTo;

  @JsonFormat(pattern = "yyyy/MM/dd")
  @DateTimeFormat(pattern = "yyyy/MM/dd")
  private LocalDate departureDate;

  @JsonFormat(pattern = "yyyy/MM/dd")
  @DateTimeFormat(pattern = "yyyy/MM/dd")
  private LocalDate returnDate;

}
