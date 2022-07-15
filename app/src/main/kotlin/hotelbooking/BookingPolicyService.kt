package hotelbooking

import hotelbooking.model.CompanyId
import hotelbooking.model.EmployeeId
import hotelbooking.model.RoomType

class BookingPolicyService {
    fun setCompanyPolicy(companyId: CompanyId, roomTypes: Collection<RoomType>) {
        TODO()
    }

    fun setEmployeePolicy(employeeId: EmployeeId, roomTypes: Collection<RoomType>) {
        TODO()
    }

    fun isBookingAllowed(employeeId: EmployeeId, roomType: RoomType): Boolean {
        TODO()
    }
}