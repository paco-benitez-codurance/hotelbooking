package hotelbooking

import hotelbooking.errors.BookingNotAllowed
import hotelbooking.errors.NotRoomTypeAvailableForThisPeriod
import hotelbooking.errors.RoomTypeNotFound
import hotelbooking.errors.WrongDates
import hotelbooking.model.Booking
import hotelbooking.model.EmployeeId
import hotelbooking.model.HotelId
import hotelbooking.model.RoomType
import java.time.LocalDate

class BookingService(
    private val dateValidator: CheckDateValidator,
    private val hotelService: HotelService,
    private val bookingPolicyService: BookingPolicyService,
    private val bookingRepository: BookingRepository
) {
    fun book(
        employeeId: EmployeeId,
        hotelId: HotelId,
        roomType: RoomType,
        checkIn: LocalDate,
        checkOut: LocalDate
    ): Booking {

        validateDates(checkIn, checkOut)
        validateHotelAndRoomType(hotelId, roomType)
        if(!bookingPolicyService.isBookingAllowed(employeeId, roomType)) {
            throw BookingNotAllowed()
        }
        val booking = Booking(employeeId, hotelId, roomType, checkIn, checkOut)
        validateHasBooked(booking)
        bookingRepository.store(booking)
        return booking
    }

    private fun validateHasBooked(booking: Booking) {
        val hotel = hotelService.findHotelBy(booking.hotelId)
        val numberOfRooms = hotel.rooms(booking.roomType)
        val numberOfBookedRooms = bookingRepository.occupiedRooms(
            booking.hotelId,
            booking.roomType,
            booking.checkIn
        )
        if(numberOfBookedRooms + 1 > numberOfRooms) {
            throw NotRoomTypeAvailableForThisPeriod()
        }
    }

    private fun validateHotelAndRoomType(hotelId: HotelId, roomType: RoomType) {
        val hotel = hotelService.findHotelBy(hotelId)
        if (!hotel.has(roomType)) {
            throw RoomTypeNotFound()
        }
    }

    private fun validateDates(checkIn: LocalDate, checkOut: LocalDate) {
        if (!dateValidator.isValid(checkIn, checkOut)) {
            throw WrongDates()
        }
    }

}