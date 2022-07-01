package hotelbooking

import java.time.LocalDate

class BookingService {
    fun book(employeeId: EmployeeId, hotelId: HotelId, roomType: RoomType, checkIn: LocalDate, checkOut: LocalDate): Booking =
        Booking(employeeId, hotelId, roomType, checkIn, checkOut)

}