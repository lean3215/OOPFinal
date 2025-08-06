package util

import java.time.LocalDate
import java.time.format.DateTimeFormatter

object Extensions:
  private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

  extension (d: LocalDate)
    def format: String = d.format(formatter)

  extension (s: String)
    def toDate: LocalDate = LocalDate.parse(s, formatter)
