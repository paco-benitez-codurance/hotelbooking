package hotelbooking

import hotelbooking.errors.HotelAlreadyExists
import hotelbooking.errors.HotelNotFound
import hotelbooking.model.Hotel
import hotelbooking.model.HotelId
import hotelbooking.model.RoomType

class HotelService {
    private var hotel: Hotel? = null

    fun addHotel(hotelId: HotelId, hotelName: String) {
        if(this.hotel?.hotelId == hotelId) {
            throw HotelAlreadyExists()
        }
        this.hotel = Hotel(hotelId)
    }

    fun setRoom(hotelId: HotelId, number: Int, roomType: RoomType) {
        validateHotelExists(hotelId)
        this.hotel = Hotel(hotelId, number, roomType)
    }

    fun findHotelBy(hotelId: HotelId): Hotel {
        validateHotelExists(hotelId)
        return hotel!!
    }

    private fun validateHotelExists(hotelId: HotelId) {
        if (this.hotel?.hotelId != hotelId) {
            throw HotelNotFound()
        }
    }
}