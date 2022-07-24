package hotelbooking

import hotelbooking.errors.CompanyPolicyDuplicated
import hotelbooking.errors.EmployeePolicyDuplicated
import hotelbooking.model.CompanyId
import hotelbooking.model.EmployeeId
import hotelbooking.model.RoomType


class BookingPolicyService {

    private var companyPolicyAdded = false
    private var employeePolicies: Array<EmployeePolicy> = emptyArray()

    data class EmployeePolicy(val employeeId: EmployeeId, val roomTypes: Collection<RoomType>) {
        fun containsRoomType(roomType: RoomType): Boolean = roomTypes.contains(roomType)

    }

    fun setCompanyPolicy(companyId: CompanyId, roomTypes: Collection<RoomType>) {
        if (companyPolicyAdded) throw CompanyPolicyDuplicated()
        companyPolicyAdded = true
    }

    fun setEmployeePolicy(employeeId: EmployeeId, roomTypes: Collection<RoomType>) {
        checkNoPolicyDuplicated(employeeId)
        employeePolicies += EmployeePolicy(employeeId, roomTypes)
    }

    fun isBookingAllowed(employeeId: EmployeeId, roomType: RoomType): Boolean {
        return employeePolicies
            .any { it.employeeId == employeeId && it.containsRoomType(roomType) }
    }

    private fun checkNoPolicyDuplicated(employeeId: EmployeeId) {
        if (isPolicyDuplicated(employeeId)) throw EmployeePolicyDuplicated()
    }

    private fun isPolicyDuplicated(employeeId: EmployeeId) = employeePolicies.map { it.employeeId }.contains(employeeId)
}