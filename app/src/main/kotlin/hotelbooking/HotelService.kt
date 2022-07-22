package hotelbooking

import hotelbooking.errors.HotelAlreadyExists
import hotelbooking.errors.HotelNotFound
import hotelbooking.model.Hotel
import hotelbooking.model.HotelId
import hotelbooking.model.RoomType
import javax.annotation.Nonnull

class HotelService {
    private var hotelId: HotelId? = null
    private var numberOfRoom: Int? = null

    fun addHotel(hotelId: HotelId, hotelName: String) {
        if(this.hotelId == hotelId) {
            throw HotelAlreadyExists()
        }
        this.hotelId = hotelId
    }

    fun setRoom(hotelId: HotelId, number: Int, roomType: RoomType) {
        if(this.hotelId != hotelId) {
            throw HotelNotFound()
        }
        this.numberOfRoom = number
    }

    fun findHotelBy(hotelId: HotelId): Hotel {
        if(this.hotelId == hotelId) {
            return Hotel(hotelId, numberOfRoom)
        }
        throw HotelNotFound()
    }
}