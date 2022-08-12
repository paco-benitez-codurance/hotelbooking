package hotelbooking

import hotelbooking.errors.*
import hotelbooking.model.*
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.time.LocalDate

/*
- [X] Booking should contain a unique ID, employeeId, hotelId, roomType, checkIn and checkOut.
- [X] Check out date must be at least one day after the check in date.
- [X] Validate if the hotel exists and room type is provided by the hotel
- [X] Verify if booking is allowed according to the booking policies defined, if any. See Booking Policy Service for more details.
- [ ] Booking should only be allowed if there is at least one room type available during the whole booking period.
- [ ] Keep track of all bookings. E.g. If hotel has 5 standard rooms, we should have no more than 5 bookings in the same day.
- [ ] Hotel rooms can be booked many times as long as there are no conflicts with the dates.
- [ ] Return booking confirmation to the employee or error otherwise (exceptions can also be used).
 */


class BookingServiceTest : StringSpec({

    val checkInDate = LocalDate.of(2022, 1, 1)
    val checkOutDate = LocalDate.of(2022, 1, 5)
    val employeeId = EmployeeId("employee id")
    val hotelId = HotelId("id1")
    val roomType = RoomType("Room Type")

    val dateValidator = mockk<CheckDateValidator>()
    val hotelService = mockk<HotelService>()
    val bookingPolicyService = mockk<BookingPolicyService>()
    val bookingRepository = mockk<BookingRepository>()
    lateinit var bookingService: BookingService

    fun setupBookingPolicyService() {
        every { bookingPolicyService.isBookingAllowed(employeeId, roomType) } returns true
    }

    fun setupHotelService() {
        val hotel = mockk<Hotel>()
        every { hotel.has(roomType) } returns true
        every { hotel.rooms(roomType) } returns 1
        every { hotelService.findHotelBy(hotelId) } returns hotel
    }

    fun setupDateValidator() {
        every { dateValidator.isValid(checkInDate, checkOutDate) }.returns(true)
    }

    fun setupBookingRepository() {
        every { bookingRepository.store(any()) } returns Unit
        every { bookingRepository.occupiedRooms(any(), any()) } returns 0
    }

    beforeTest {
        setupDateValidator()
        setupHotelService()
        setupBookingPolicyService()
        setupBookingRepository()

        bookingService = BookingService(dateValidator, hotelService, bookingPolicyService, bookingRepository)
    }

    "Booking should contain a unique ID, employeeId, hotelId, roomType, checkIn and checkOut" {

        val booking = bookingService.book(
            employeeId, hotelId, roomType, checkInDate, checkOutDate
        )

        booking shouldBe Booking(
            employeeId, hotelId, roomType, checkInDate, checkOutDate
        )
    }

    "Exception should be raise when check date fails" {

        val wrongDate = mockk<LocalDate>()
        every { dateValidator.isValid(checkInDate, wrongDate) }.returns(false)

        shouldThrow<WrongDates> {
            bookingService.book(
                employeeId, hotelId, roomType, checkInDate, wrongDate
            )
        }

        verify { dateValidator.isValid(checkInDate, wrongDate) }
    }

    "Exception should be raise when hotel is not found" {
        val nonExistingHotelId = HotelId("nonExistingId")

        every { hotelService.findHotelBy(nonExistingHotelId) } throws HotelNotFound()

        shouldThrow<HotelNotFound> {
            bookingService.book(
                employeeId, nonExistingHotelId, roomType, checkInDate, checkOutDate
            )
        }
    }

    "Exception should be raise when room type is not found" {
        val hotel = mockk<Hotel>()
        every { hotel.has(roomType) } returns false
        every { hotelService.findHotelBy(hotelId) } returns hotel

        shouldThrow<RoomTypeNotFound> {
            bookingService.book(
                employeeId, hotelId, roomType, checkInDate, checkOutDate
            )
        }
    }

    "Exception should be raise when policy denied the room" {

        val notAllowedType = mockk<RoomType>()

        val hotel = mockk<Hotel>()
        every { hotel.has(notAllowedType) } returns true
        every { hotelService.findHotelBy(hotelId) } returns hotel

        every { bookingPolicyService.isBookingAllowed(employeeId, notAllowedType) } returns false

        shouldThrow<BookingNotAllowed> {
            bookingService.book(
                employeeId, hotelId, notAllowedType, checkInDate, checkOutDate
            )
        }
    }

    "Booking should be stored in BookingRepository" {
        val booking = bookingService.book(
            employeeId, hotelId, roomType, checkInDate, checkOutDate
        )
        verify { bookingRepository.store(booking) }
    }

    "Booking should only be allowed if there is at least one room type available during the whole booking period" {
        every { hotelService.findHotelBy(hotelId) } returns Hotel(hotelId, mapOf(roomType to 1))
        every { bookingRepository.occupiedRooms(hotelId, roomType) } returns 1

        shouldThrow<NotRoomTypeAvailableForThisPeriod> {
            bookingService.book(
                employeeId, hotelId, roomType, checkInDate, checkOutDate
            )
        }
    }

})

