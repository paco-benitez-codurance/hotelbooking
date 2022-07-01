package hotelbooking

class BookingPolicyService<CompanyId, RoomTypes, RoomType, EmployeeId> {
    fun setCompanyPolicy(companyId: CompanyId, roomTypes: RoomTypes) {
        TODO()
    }

    fun setEmployeePolicy(employeeId: EmployeeId, roomTypes: RoomTypes) {
        TODO()
    }

    fun isBookingAllowed(employeeId: EmployeeId, roomType: RoomType): Boolean {
        TODO()
    }
}