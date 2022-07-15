package hotelbooking

import hotelbooking.errors.HotelAlreadyExists
import hotelbooking.errors.HotelNotFound
import hotelbooking.model.Hotel
import hotelbooking.model.HotelId
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith


class HotelServiceTest {
    companion object {
        private val HOTEL_ID = HotelId("id1")
        private const val HOTEL_NAME = "Hotel Name"
    }

    private lateinit var hotelService: HotelService

    @Before
    fun setUp() {
        hotelService = HotelService()
    }

    @Test
    fun exception_when_hotelAlreadyExists() {
        hotelService.addHotel(HOTEL_ID, HOTEL_NAME)
        assertFailsWith<HotelAlreadyExists> {
            hotelService.addHotel(HOTEL_ID, HOTEL_NAME)
        }
    }

    @Test
    fun exception_when_hotelNotFound() {
        assertFailsWith<HotelNotFound> {
            hotelService.findHotelBy(HOTEL_ID)
        }
    }

    @Test
    fun hotelInfo_when_hotelFound() {
        hotelService.addHotel(HOTEL_ID, HOTEL_NAME)

        val expected = Hotel(HOTEL_ID)
        val hotel = hotelService.findHotelBy(HOTEL_ID)

        assertEquals(expected, hotel)
    }
}