package hotelbooking

import hotelbooking.model.Booking
import hotelbooking.model.HotelId
import hotelbooking.model.RoomType

class BookingRepository {
    private var bookings: List<Booking> = emptyList()

    fun store(booking: Booking) {
        bookings = bookings + booking
    }

    fun occupiedRooms(hotelId: HotelId, roomType: RoomType): Int {
        return bookings.count { it.hotelId == hotelId && it.roomType == roomType }
    }

}
