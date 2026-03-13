package com.Wissam.EasyApplier.Model

import jakarta.persistence.Entity
import lombok.AllArgsConstructor

@Entity
@AllArgsConstructor
class User2 {
  var name: String = ""
  var age: Int = 0
}
