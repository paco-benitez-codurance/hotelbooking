package hotelbooking

import hotelbooking.errors.RoomTypeNotFound
import hotelbooking.errors.WrongDates
import hotelbooking.model.Booking
import hotelbooking.model.EmployeeId
import hotelbooking.model.HotelId
import hotelbooking.model.RoomType
import java.time.LocalDate

class BookingService(private val dateValidator: CheckDateValidator, private val hotelService: HotelService) {
    fun book(
        employeeId: EmployeeId,
        hotelId: HotelId,
        roomType: RoomType,
        checkIn: LocalDate,
        checkOut: LocalDate
    ): Booking {
        if (!dateValidator.isValid(checkIn, checkOut)) {
            throw WrongDates()
        }
        val hotel = hotelService.findHotelBy(hotelId)
        if (!hotel.has(roomType)) {
            throw RoomTypeNotFound()
        }
        return Booking(employeeId, hotelId, roomType, checkIn, checkOut)
    }

}