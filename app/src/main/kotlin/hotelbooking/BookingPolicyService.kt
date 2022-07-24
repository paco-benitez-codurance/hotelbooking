package hotelbooking

import hotelbooking.errors.CompanyPolicyDuplicated
import hotelbooking.errors.EmployeePolicyDuplicated
import hotelbooking.model.CompanyId
import hotelbooking.model.EmployeeId
import hotelbooking.model.RoomType


class BookingPolicyService {

    var companyPolicyAdded = false
    var employeePolicyAdded: Array<EmployeeId> = emptyArray()
    fun setCompanyPolicy(companyId: CompanyId, roomTypes: Collection<RoomType>) {
        if(companyPolicyAdded) throw CompanyPolicyDuplicated()
        companyPolicyAdded = true
    }

    fun setEmployeePolicy(employeeId: EmployeeId, roomTypes: Collection<RoomType>) {
        if(employeePolicyAdded.contains(employeeId)) throw EmployeePolicyDuplicated()
        employeePolicyAdded = employeePolicyAdded + employeeId
    }

    fun isBookingAllowed(employeeId: EmployeeId, roomType: RoomType): Boolean {
        return employeePolicyAdded.contains(employeeId)
    }
}