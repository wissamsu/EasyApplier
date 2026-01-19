package com.Wissam.EasyApplier.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "job_answers")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobAnswer {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  private String address;

  private String city;

  private String state;

  private String zipCode;

  private String country;

  private String linkedinProfileUrl;

  private Short yearsOfExperience;

  private Boolean eligibleToWorkInUS;

  private Boolean requireVisa;

}
