package hotelbooking

import hotelbooking.errors.HotelAlreadyExists
import hotelbooking.errors.HotelNotFound
import hotelbooking.model.Hotel
import hotelbooking.model.HotelId
import hotelbooking.model.RoomType

class HotelService {
    private var hotel: Hotel? = null

    fun addHotel(hotelId: HotelId, hotelName: String) {
        if (exists(hotelId)) {
            throw HotelAlreadyExists()
        }
        this.add(Hotel(hotelId))
    }


    fun setRoom(hotelId: HotelId, number: Int, roomType: RoomType) {
        validateHotelExists(hotelId)
        this.add(Hotel(hotelId, number, roomType))
    }

    fun findHotelBy(hotelId: HotelId): Hotel {
        validateHotelExists(hotelId)
        return hotel!!
    }

    private fun add(hotel: Hotel) {
        this.hotel = hotel
    }

    private fun exists(hotelId: HotelId) = this.hotel?.hotelId == hotelId

    private fun validateHotelExists(hotelId: HotelId) {
        if (!exists(hotelId)) {
            throw HotelNotFound()
        }
    }
}