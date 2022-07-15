package hotelbooking

import hotelbooking.errors.HotelNotFound
import hotelbooking.errors.RoomTypeNotFound
import hotelbooking.errors.WrongDates
import hotelbooking.model.*
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import java.time.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

/*
- [X] Booking should contain a unique ID, employeeId, hotelId, roomType, checkIn and checkOut.
- [X] Check out date must be at least one day after the check in date.
- [X] Validate if the hotel exists and room type is provided by the hotel
- [ ] Verify if booking is allowed according to the booking policies defined, if any. See Booking Policy Service for more details.
- [ ] Booking should only be allowed if there is at least one room type available during the whole booking period.
- [ ] Keep track of all bookings. E.g. If hotel has 5 standard rooms, we should have no more than 5 bookings in the same day.
- [ ] Hotel rooms can be booked many times as long as there are no conflicts with the dates.
- [ ] Return booking confirmation to the employee or error otherwise (exceptions can also be used).
 */


class BookingServiceTest {
    companion object {
        private val CHECKIN_DATE = LocalDate.of(2022, 1, 1)
        private val CHECKOUT_DATE = LocalDate.of(2022, 1, 5)
        private val EMPLOYEE_ID = EmployeeId()
        private val HOTEL_ID = HotelId("id1")
        private val ROOM_TYPE = RoomType()
    }

    private val dateValidator = mockk<CheckDateValidator>()
    private val hotelService = mockk<HotelService>()
    private var bookingService = BookingService(dateValidator, hotelService)


    @Before
    fun setUp() {
        every { dateValidator.isValid(CHECKIN_DATE, CHECKOUT_DATE) }.returns(true)

        val hotel = mockk<Hotel>()
        every { hotel.has(ROOM_TYPE) } returns true
        every { hotelService.findHotelBy(HOTEL_ID) } returns hotel


        bookingService = BookingService(dateValidator, hotelService)
    }

    @Test
    fun should_returnBookingObject() {

        val booking = bookingService.book(
            EMPLOYEE_ID, HOTEL_ID, ROOM_TYPE, CHECKIN_DATE, CHECKOUT_DATE
        )

        assertEquals(
            booking, Booking(
                EMPLOYEE_ID, HOTEL_ID, ROOM_TYPE, CHECKIN_DATE, CHECKOUT_DATE
            )
        )
    }

    @Test
    fun exceptionShouldBeThrown_when_checkDateFails() {

        val wrongDate = mockk<LocalDate>()
        every { dateValidator.isValid(CHECKIN_DATE, wrongDate) }.returns(false)

        assertFailsWith<WrongDates> {
            bookingService.book(
                EMPLOYEE_ID, HOTEL_ID, ROOM_TYPE, CHECKIN_DATE, wrongDate
            )
        }

        verify { dateValidator.isValid(CHECKIN_DATE, wrongDate) }
    }

    @Test
    fun exception_when_hotelIsNotFound() {
        val nonExistingHotelId = HotelId("nonExistingId")

        every { hotelService.findHotelBy(nonExistingHotelId) } throws HotelNotFound()

        assertFailsWith<HotelNotFound> {
            bookingService.book(
                EMPLOYEE_ID, nonExistingHotelId, ROOM_TYPE, CHECKIN_DATE, CHECKOUT_DATE
            )
        }
    }

    @Test
    fun exception_when_roomTypeIsNotFound() {
        val hotel = mockk<Hotel>()
        every { hotel.has(ROOM_TYPE) } returns false
        every { hotelService.findHotelBy(HOTEL_ID) } returns hotel

        assertFailsWith<RoomTypeNotFound> {
            bookingService.book(
                EMPLOYEE_ID, HOTEL_ID, ROOM_TYPE, CHECKIN_DATE, CHECKOUT_DATE
            )
        }
    }

    /*
    @Test
    fun validateIfHotelAndRoomTypeAreProvidedByTheHotel() {
        val hotelService = HotelService<String>()

        hotelService.addHotel(HOTEL_ID, HOTEL_NAME)
        hotelService.setRoom(HOTEL_ID, 3, ROOM_TYPE)

        val booking = bookingService.book(
            EMPLOYEE_ID, HOTEL_ID, ROOM_TYPE, CHECKIN_DATE, CHECKOUT_DATE)

        assertEquals(
            booking, Booking(
                EMPLOYEE_ID, HOTEL_ID, ROOM_TYPE, CHECKIN_DATE, CHECKOUT_DATE
            )
        )
    }
     */

}
