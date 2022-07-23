package hotelbooking

import hotelbooking.errors.CompanyPolicyDuplicated
import hotelbooking.errors.EmployeePolicyDuplicated
import hotelbooking.model.CompanyId
import hotelbooking.model.EmployeeId
import hotelbooking.model.RoomType


class BookingPolicyService {

    var companyPolicyAdded = false
    var employeePolicyAdded = false
    fun setCompanyPolicy(companyId: CompanyId, roomTypes: Collection<RoomType>) {
        if(companyPolicyAdded) throw CompanyPolicyDuplicated()
        companyPolicyAdded = true
    }

    fun setEmployeePolicy(employeeId: EmployeeId, roomTypes: Collection<RoomType>) {
        if(employeePolicyAdded) throw EmployeePolicyDuplicated()
        employeePolicyAdded = true
    }

    fun isBookingAllowed(employeeId: EmployeeId, roomType: RoomType): Boolean {
        TODO()
    }
}