package hotelbooking

import java.time.LocalDate

class CheckDateValidator {
    fun isValid(checkIn: LocalDate, checkOut: LocalDate) = checkOut.isAfter(checkIn);
}
