package com.Wissam.EasyApplier.Extractions.Jsoup.Models;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FlightsFound {

  private BigDecimal price;
  private String link;

}
