package hotelbooking

import hotelbooking.errors.HotelAlreadyExists
import hotelbooking.errors.HotelNotFound
import hotelbooking.model.Hotel
import hotelbooking.model.HotelId
import hotelbooking.model.RoomType
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.assertThrows

class HotelServiceTest : StringSpec({

    val HOTEL_ID = HotelId("id1")
    val HOTEL_NAME = "Hotel Name"
    val ROOM_TYPE = RoomType()

    lateinit var hotelService: HotelService

    beforeEach {
        hotelService = HotelService()
    }

    "Exception should be raise when hotel already exists" {

        hotelService.addHotel(HOTEL_ID, HOTEL_NAME)
        assertThrows<HotelAlreadyExists> {
            hotelService.addHotel(HOTEL_ID, HOTEL_NAME)
        }
    }

    "Exception should be raise when hotel not found" {
        assertThrows<HotelNotFound> {
            hotelService.findHotelBy(HOTEL_ID)
        }
    }

    "Return hotel info when found" {
        hotelService.addHotel(HOTEL_ID, HOTEL_NAME)

        val expected = Hotel(HOTEL_ID)
        val hotel = hotelService.findHotelBy(HOTEL_ID)

        hotel shouldBe expected
    }


    "Return hotel with no room when found" {
        hotelService.addHotel(HOTEL_ID, HOTEL_NAME)

        val hotel = hotelService.findHotelBy(HOTEL_ID)

        hotel.rooms(ROOM_TYPE) shouldBe 0
    }
})