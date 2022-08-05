package hotelbooking

import hotelbooking.errors.HotelAlreadyExists
import hotelbooking.errors.HotelNotFound
import hotelbooking.model.Hotel
import hotelbooking.model.HotelId
import hotelbooking.model.RoomType

class HotelService {
    private var hotels: List<Hotel> = emptyList()

    fun addHotel(hotelId: HotelId, hotelName: String) {
        if (exists(hotelId)) {
            throw HotelAlreadyExists()
        }
        this.hotels = this.hotels + Hotel(hotelId)
    }


    fun setRoom(hotelId: HotelId, number: Int, roomType: RoomType) {
        if (!exists(hotelId)) {
            throw HotelNotFound()
        }
        this.add(hotelId, number, roomType)
    }

    fun findHotelBy(hotelId: HotelId): Hotel {
        return hotel(hotelId) ?: throw HotelNotFound()
    }

    private fun hotel(hotelId: HotelId) = hotels.find { it.hotelId == hotelId }

    private fun add(hotelId: HotelId, number: Int, roomType: RoomType) {
        this.hotels = this.hotels.map {
            if (it.hotelId == hotelId)
                Hotel( hotelId, it.roomTypes + Pair(roomType, number) )
            else it
        }
    }

    private fun exists(hotelId: HotelId) = this.hotels.count { it.hotelId == hotelId } > 0

}