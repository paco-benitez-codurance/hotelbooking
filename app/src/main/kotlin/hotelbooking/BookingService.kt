package hotelbooking

import java.time.LocalDate

class BookingService(val dateValidator: CheckDateValidator, val hotelService: HotelService) {
    fun book(employeeId: EmployeeId, hotelId: HotelId, roomType: RoomType, checkIn: LocalDate, checkOut: LocalDate): Booking {
        if(!dateValidator.isValid(checkIn, checkOut)) {
            throw WrongDates()
        }
        hotelService.findHotelBy(hotelId)
        return Booking(employeeId, hotelId, roomType, checkIn, checkOut)
    }

}