package hotelbooking

import hotelbooking.model.Booking
import hotelbooking.model.HotelId
import hotelbooking.model.RoomType
import java.time.LocalDate

class BookingRepository {
    private var bookings: List<Booking> = emptyList()

    fun store(booking: Booking) {
        bookings = bookings + booking
    }

    fun occupiedRooms(hotelId: HotelId, roomType: RoomType, checkInDate: LocalDate): Int {
        return bookings.count {
            it.hotelId == hotelId && it.roomType == roomType && sameDaysOfBooking(
                it.checkIn,
                it.checkOut,
                checkInDate
            )
        }
    }

    private fun sameDaysOfBooking(
        checkIn: LocalDate,
        checkOut: LocalDate,
        possibleCheckIn: LocalDate
    ): Boolean {
        return checkIn.rangeTo(checkOut.minusDays(1)).contains(possibleCheckIn)
    }

}
