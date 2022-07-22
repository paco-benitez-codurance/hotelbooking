package hotelbooking

import hotelbooking.errors.HotelAlreadyExists
import hotelbooking.errors.HotelNotFound
import hotelbooking.model.Hotel
import hotelbooking.model.HotelId
import hotelbooking.model.RoomType
import io.kotest.core.spec.style.FreeSpec
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.assertThrows

class HotelServiceTest : FreeSpec({

    val hotelId = HotelId("id1")
    val nonExistingHotel = HotelId("nonExistingHotel")
    val hotelName = "Hotel Name"
    val roomType = RoomType("room type")

    lateinit var hotelService: HotelService

    beforeEach {
        hotelService = HotelService()
    }

    "Hotel Management" - {

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
    }


    "Room Management" - {

        fun setupHotelAndRoom(numberOfRooms: Int) {
            hotelService.addHotel(hotelId, hotelName)
            hotelService.setRoom(hotelId, numberOfRooms, roomType)
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

        "number of room should be 0 when no type found" {
            val numberOfRooms = 5

            setupHotelAndRoom(numberOfRooms)

            val hotel = hotelService.findHotelBy(hotelId)

            val otherRoomType = RoomType("out of service room type")
            hotel.rooms(otherRoomType) shouldBe 0
        }


        "hotel info should return the number of room by room type" {
            val numberOfRooms = 5

            setupHotelAndRoom(numberOfRooms)

            val hotel = hotelService.findHotelBy(hotelId)

            hotel.rooms(roomType) shouldBe numberOfRooms
        }

        "hotel info should has room type" {
            val numberOfRooms = 5

            setupHotelAndRoom(numberOfRooms)

            val hotel = hotelService.findHotelBy(hotelId)

            hotel.has(roomType) shouldBe true
        }
    }
})