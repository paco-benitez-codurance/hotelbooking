package hotelbooking

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.time.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

/*
- [X] Booking should contain a unique ID, employeeId, hotelId, roomType, checkIn and checkOut.
- [X] Check out date must be at least one day after the check in date.
- [ ] Validate if the hotel exists and room type is provided by the hotel
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
        private val HOTEL_ID = HotelId()
        private val ROOM_TYPE = RoomType()
    }

    private val dateValidator = mockk<CheckDateValidator>()
    private val bookingService = BookingService(dateValidator)

    @Test
    fun should_returnBookingObject() {
        every { dateValidator.isValid(CHECKIN_DATE, CHECKOUT_DATE) }.returns(true)

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
        every { dateValidator.isValid(CHECKIN_DATE, CHECKOUT_DATE) }.returns(false)

        assertFailsWith<WrongDates> {
            bookingService.book(
                EMPLOYEE_ID, HOTEL_ID, ROOM_TYPE, CHECKIN_DATE, CHECKOUT_DATE
            )
        }

        verify { dateValidator.isValid(CHECKIN_DATE, CHECKOUT_DATE) }


    }

}
