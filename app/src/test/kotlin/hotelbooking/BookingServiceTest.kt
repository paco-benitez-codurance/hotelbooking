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

    val CHECKIN_DATE = LocalDate.of(2022, 1, 1)
    val CHECKOUT_DATE = LocalDate.of(2022, 1, 5)
    val EMPLOYEE_ID = EmployeeId("employee id")
    val HOTEL_ID = HotelId("id1")
    val ROOM_TYPE = RoomType("Room Type")

    val dateValidator = mockk<CheckDateValidator>()
    val hotelService = mockk<HotelService>()
    val bookingPolicyService = mockk<BookingPolicyService>()
    lateinit var bookingService: BookingService

    fun setupBookingPolicyService() {
        every { bookingPolicyService.isBookingAllowed(EMPLOYEE_ID, ROOM_TYPE) } returns true
    }

    fun setupHotelService() {
        val hotel = mockk<Hotel>()
        every { hotel.has(ROOM_TYPE) } returns true
        every { hotelService.findHotelBy(HOTEL_ID) } returns hotel
    }

    fun setupDateValidator() {
        every { dateValidator.isValid(CHECKIN_DATE, CHECKOUT_DATE) }.returns(true)
    }

    beforeTest {
        setupDateValidator()
        setupHotelService()
        setupBookingPolicyService()

        bookingService = BookingService(dateValidator, hotelService, bookingPolicyService)
    }

    "Booking should contain a unique ID, employeeId, hotelId, roomType, checkIn and checkOut" {

        val booking = bookingService.book(
            EMPLOYEE_ID, HOTEL_ID, ROOM_TYPE, CHECKIN_DATE, CHECKOUT_DATE
        )

        booking shouldBe Booking(
            EMPLOYEE_ID, HOTEL_ID, ROOM_TYPE, CHECKIN_DATE, CHECKOUT_DATE
        )
    }

    "Exception should be raise when check date fails" {

        val wrongDate = mockk<LocalDate>()
        every { dateValidator.isValid(CHECKIN_DATE, wrongDate) }.returns(false)

        shouldThrow<WrongDates> {
            bookingService.book(
                EMPLOYEE_ID, HOTEL_ID, ROOM_TYPE, CHECKIN_DATE, wrongDate
            )
        }

        verify { dateValidator.isValid(CHECKIN_DATE, wrongDate) }
    }

    "Exception should be raise when hotel is not found" {
        val nonExistingHotelId = HotelId("nonExistingId")

        every { hotelService.findHotelBy(nonExistingHotelId) } throws HotelNotFound()

        shouldThrow<HotelNotFound> {
            bookingService.book(
                EMPLOYEE_ID, nonExistingHotelId, ROOM_TYPE, CHECKIN_DATE, CHECKOUT_DATE
            )
        }
    }

    "Exception should be raise when room type is not found" {
        val hotel = mockk<Hotel>()
        every { hotel.has(ROOM_TYPE) } returns false
        every { hotelService.findHotelBy(HOTEL_ID) } returns hotel

        shouldThrow<RoomTypeNotFound> {
            bookingService.book(
                EMPLOYEE_ID, HOTEL_ID, ROOM_TYPE, CHECKIN_DATE, CHECKOUT_DATE
            )
        }
    }

    "Exception should be raise when policy denied the room" {

        val notAllowedType = mockk<RoomType>()

        val hotel = mockk<Hotel>()
        every { hotel.has(notAllowedType) } returns true
        every { hotelService.findHotelBy(HOTEL_ID) } returns hotel

        every { bookingPolicyService.isBookingAllowed(EMPLOYEE_ID, notAllowedType) } returns false

        shouldThrow<BookingNotAllowed> {
            bookingService.book(
                EMPLOYEE_ID, HOTEL_ID, notAllowedType, CHECKIN_DATE, CHECKOUT_DATE
            )
        }
    }

    /*
    "Booking should only be allowed if there is at least one room type available during the whole booking period" {
        every { hotelService.findHotelBy(HOTEL_ID) } returns Hotel(HOTEL_ID, mapOf(ROOM_TYPE to 1))


        bookingService.book(
            EMPLOYEE_ID, HOTEL_ID, ROOM_TYPE, CHECKIN_DATE, CHECKOUT_DATE
        )
        shouldThrow<NotRoomTypeAvailableForThisPeriod> {
            bookingService.book(
                EMPLOYEE_ID, HOTEL_ID, ROOM_TYPE, CHECKIN_DATE, CHECKOUT_DATE
            )
        }
    }
     */


})
