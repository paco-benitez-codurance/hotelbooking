package hotelbooking

import java.time.LocalDate

data class Booking(
    val employeeId: EmployeeId,
    val hotelId: HotelId,
    val roomType: RoomType,
    val checkIn: LocalDate,
    val checkOut: LocalDate
)
