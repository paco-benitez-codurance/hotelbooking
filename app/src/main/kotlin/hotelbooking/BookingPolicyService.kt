package hotelbooking

import hotelbooking.errors.CompanyPolicyDuplicated
import hotelbooking.errors.EmployeePolicyDuplicated
import hotelbooking.model.CompanyId
import hotelbooking.model.EmployeeId
import hotelbooking.model.RoomType


class BookingPolicyService(val belongable: Belongable) {

    private var companyPolicies: Array<CompanyPolicy> = emptyArray()
    private var employeePolicies: Array<EmployeePolicy> = emptyArray()

    data class EmployeePolicy(val employeeId: EmployeeId, val roomTypes: Collection<RoomType>) {
        fun containsRoomType(roomType: RoomType): Boolean = roomTypes.contains(roomType)
    }

    data class CompanyPolicy(val companyId: CompanyId, val roomTypes: Collection<RoomType>) {
        fun containsRoomType(roomType: RoomType): Boolean = roomTypes.contains(roomType)
    }


    fun setCompanyPolicy(companyId: CompanyId, roomTypes: Collection<RoomType>) {
        checkNoPolicyDuplicated(companyId)
        companyPolicies += CompanyPolicy(companyId, roomTypes)
    }

    fun setEmployeePolicy(employeeId: EmployeeId, roomTypes: Collection<RoomType>) {
        checkNoPolicyDuplicated(employeeId)
        employeePolicies += EmployeePolicy(employeeId, roomTypes)
    }

    fun isBookingAllowed(employeeId: EmployeeId, roomType: RoomType): Boolean {
        return isEmployeeBookingAllowed(employeeId, roomType)  || isCompanyBookingAllowed(employeeId, roomType)
    }

    private fun isCompanyBookingAllowed(employeeId: EmployeeId, roomType: RoomType): Boolean {
        val companyId = belongable.company(employeeId)
        return companyPolicies
            .any { it.companyId == companyId && it.containsRoomType(roomType) }
    }

    private fun isEmployeeBookingAllowed(
        employeeId: EmployeeId,
        roomType: RoomType
    ): Boolean {
        return employeePolicies
            .any { it.employeeId == employeeId && it.containsRoomType(roomType) }
    }

    private fun checkNoPolicyDuplicated(employeeId: EmployeeId) {
        if (isPolicyDuplicated(employeeId)) throw EmployeePolicyDuplicated()
    }
    private fun isPolicyDuplicated(employeeId: EmployeeId) = employeePolicies.map { it.employeeId }.contains(employeeId)

    private fun checkNoPolicyDuplicated(companyId: CompanyId) {
        if (isPolicyDuplicated(companyId)) throw CompanyPolicyDuplicated()
    }
    private fun isPolicyDuplicated(companyId: CompanyId) = companyPolicies.map { it.companyId }.contains(companyId)
}
