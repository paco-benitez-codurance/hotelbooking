package hotelbooking

import hotelbooking.errors.CompanyPolicyDuplicated
import hotelbooking.errors.EmployeePolicyDuplicated
import hotelbooking.model.CompanyId
import hotelbooking.model.EmployeeId
import hotelbooking.model.RoomType
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec


class BookingPolicyServiceTest: StringSpec({

    val companyId = CompanyId("company id")
    val roomType = RoomType("roomType")
    val employeeId = EmployeeId("employee id")

    lateinit var bookingPolicyService: BookingPolicyService

    beforeEach{
        bookingPolicyService = BookingPolicyService()
    }


    "Company policy cannot be duplicated" {
        val book = {
            bookingPolicyService.setCompanyPolicy(companyId, listOf(roomType))
        }
        book()
        shouldThrow<CompanyPolicyDuplicated> {
            book()
        }
    }

    "Employee policy cannot be duplicated" {
        val book = {
            bookingPolicyService.setEmployeePolicy(employeeId, listOf(roomType))
        }
        book()
        shouldThrow<EmployeePolicyDuplicated> {
            book()
        }
    }

})