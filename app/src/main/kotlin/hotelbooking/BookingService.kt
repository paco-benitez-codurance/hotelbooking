package hotelbooking

import java.time.LocalDate

class BookingService(val dateValidator: CheckDateValidator) {
    fun book(employeeId: EmployeeId, hotelId: HotelId, roomType: RoomType, checkIn: LocalDate, checkOut: LocalDate): Booking {
        if(!dateValidator.isValid(checkIn, checkOut)) {
            throw WrongDates()
        }
        return Booking(employeeId, hotelId, roomType, checkIn, checkOut)
    }

}