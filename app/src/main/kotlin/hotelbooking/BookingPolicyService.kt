package hotelbooking

import hotelbooking.errors.CompanyPolicyDuplicated
import hotelbooking.errors.EmployeePolicyDuplicated
import hotelbooking.model.CompanyId
import hotelbooking.model.EmployeeId
import hotelbooking.model.RoomType


class BookingPolicyService(private val belongable: Belongable) {

    private var companyPolicy: RoomTypePolicy<CompanyId> = RoomTypePolicy.empty()
    private var employeePolicy: RoomTypePolicy<EmployeeId> = RoomTypePolicy.empty()


    fun setCompanyPolicy(companyId: CompanyId, roomTypes: Collection<RoomType>) {
        if (!companyPolicy.addPolicy(companyId, roomTypes)) throw CompanyPolicyDuplicated()
    }

    fun setEmployeePolicy(employeeId: EmployeeId, roomTypes: Collection<RoomType>) {
        if (!employeePolicy.addPolicy(employeeId, roomTypes)) throw EmployeePolicyDuplicated()
    }

    fun isBookingAllowed(employeeId: EmployeeId, roomType: RoomType): Boolean {
        return isEmployeeBookingAllowed(employeeId, roomType) || isCompanyBookingAllowed(employeeId, roomType)
    }

    private fun isCompanyBookingAllowed(
        employeeId: EmployeeId,
        roomType: RoomType
    ): Boolean {
        val companyId = belongable.company(employeeId) ?: return false
        return companyPolicy.isBookingAllowed(companyId, roomType)
    }

    private fun isEmployeeBookingAllowed(
        employeeId: EmployeeId,
        roomType: RoomType
    ): Boolean {
        return employeePolicy.isBookingAllowed(employeeId, roomType)
    }

    internal class RoomTypePolicy<ItemId> {

        private var policies: Array<RoomTypePolicyItem<ItemId>> = emptyArray()

        companion object {
            fun <ItemId> empty() = RoomTypePolicy<ItemId>()
        }

        fun addPolicy(itemId: ItemId, roomTypes: Collection<RoomType>): Boolean {
            if (isPolicyDuplicated(itemId)) return false
            policies += RoomTypePolicyItem(itemId, roomTypes)
            return true

        }

        private fun isPolicyDuplicated(itemId: ItemId) = policies.map { it.item }.contains(itemId)

        fun isBookingAllowed(
            itemId: ItemId,
            roomType: RoomType
        ): Boolean {
            return policies
                .any { it.item == itemId && it.containsRoomType(roomType) }
        }

        private data class RoomTypePolicyItem<ItemId>(
            val item: ItemId,
            val roomTypes: Collection<RoomType>
        ) {
            fun containsRoomType(roomType: RoomType): Boolean = roomTypes.contains(roomType)
        }

    }


}
