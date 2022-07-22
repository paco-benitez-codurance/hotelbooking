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

    val hotelId = HotelId("id1")
    val nonExistingHotel = HotelId("nonExistingHotel")
    val hotelName = "Hotel Name"
    val roomType = RoomType()

    lateinit var hotelService: HotelService

    beforeEach {
        hotelService = HotelService()
    }

    "Exception should be raise when hotel already exists" {

        hotelService.addHotel(hotelId, hotelName)
        assertThrows<HotelAlreadyExists> {
            hotelService.addHotel(hotelId, hotelName)
        }
    }

    "Exception should be raise when hotel not found" {
        assertThrows<HotelNotFound> {
            hotelService.findHotelBy(hotelId)
        }
    }

    "Return hotel info when found" {
        hotelService.addHotel(hotelId, hotelName)

        val expected = Hotel(hotelId)
        val hotel = hotelService.findHotelBy(hotelId)

        hotel shouldBe expected
    }


    "Return hotel with no room when found" {
        hotelService.addHotel(hotelId, hotelName)

        val hotel = hotelService.findHotelBy(hotelId)

        hotel.rooms(roomType) shouldBe 0
    }


    "set room should fail when hotel not found" {
        val numberOfRooms = 5

        assertThrows<HotelNotFound> {
            hotelService.setRoom(nonExistingHotel, numberOfRooms, roomType)
        }
    }


    "hotel info should have the hotel room set" {
        val numberOfRooms = 5

        hotelService.addHotel(hotelId, hotelName)
        hotelService.setRoom(hotelId, numberOfRooms, roomType)

        val hotel = hotelService.findHotelBy(hotelId)

        hotel.rooms(roomType) shouldBe numberOfRooms
    }
})