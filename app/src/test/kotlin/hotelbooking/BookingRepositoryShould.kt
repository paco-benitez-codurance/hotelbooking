package hotelbooking

import hotelbooking.model.Booking
import hotelbooking.model.EmployeeId
import hotelbooking.model.HotelId
import hotelbooking.model.RoomType
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate

class BookingRepositoryShould : StringSpec({
    val checkInDate = LocalDate.of(2022, 1, 1)
    val checkoutDate = LocalDate.of(2022, 1, 5)
    val employeeId = EmployeeId("employee id")
    val hotelId1 = HotelId("id1")
    val hotelId2 = HotelId("id2")
    val roomType = RoomType("Room Type")

    lateinit var bookingRepository: BookingRepository

    beforeEach {
        bookingRepository = BookingRepository()
    }

    "After store a booking should return occupation" {
        val booking = Booking(
            employeeId, hotelId1, roomType, checkInDate, checkoutDate
        )
        bookingRepository.store(booking)

        bookingRepository.occupiedRooms(hotelId1, roomType, checkInDate) shouldBe 1
    }

    "should return 0 if no store is done for this roomType " {
        val booking = Booking(
            employeeId, hotelId1, roomType, checkInDate, checkoutDate
        )
        bookingRepository.store(booking)

        bookingRepository.occupiedRooms(hotelId1, RoomType("nonstoreroomtype"), checkInDate) shouldBe 0
    }

    "After store different bookings should return its occupation" {
        val booking1 = Booking(
            employeeId, hotelId1, roomType, checkInDate, checkoutDate
        )
        bookingRepository.store(booking1)

        val booking2 = Booking(
            employeeId, hotelId2, roomType, checkInDate, checkoutDate
        )
        bookingRepository.store(booking2)
        bookingRepository.store(booking2)

        bookingRepository.occupiedRooms(hotelId1, roomType, checkInDate) shouldBe 1
        bookingRepository.occupiedRooms(hotelId2, roomType, checkInDate) shouldBe 2
    }

    "should return 0 if a booking is same day that other booking ends" {
        val booking = Booking(
            employeeId, hotelId1, roomType, checkInDate, checkoutDate
        )
        bookingRepository.store(booking)

        bookingRepository.occupiedRooms(hotelId1, roomType, checkoutDate) shouldBe 0
    }

    "should return 1 if a booking is in the middle that other booking" {
        val booking = Booking(
            employeeId, hotelId1, roomType, checkInDate, checkoutDate
        )
        bookingRepository.store(booking)

        bookingRepository.occupiedRooms(hotelId1, roomType, checkoutDate.minusDays(2)) shouldBe 1
    }
})
