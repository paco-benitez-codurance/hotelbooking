package hotelbooking

import hotelbooking.errors.CompanyPolicyDuplicated
import hotelbooking.errors.EmployeePolicyDuplicated
import hotelbooking.model.CompanyId
import hotelbooking.model.EmployeeId
import hotelbooking.model.RoomType
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec

import io.kotest.matchers.shouldBe


class BookingPolicyServiceTest : FreeSpec({

    val companyId = CompanyId("company id")
    val roomType = RoomType("roomType")
    val roomTypes = listOf(roomType)
    val employeeId = EmployeeId("employee id")

    lateinit var bookingPolicyService: BookingPolicyService

    beforeEach {
        bookingPolicyService = BookingPolicyService()
    }


    "Basic" - {

        "Company policy cannot be duplicated" {
            val book = {
                bookingPolicyService.setCompanyPolicy(companyId, roomTypes)
            }
            book()
            shouldThrow<CompanyPolicyDuplicated> {
                book()
            }
        }

        "Employee policy cannot be duplicated" {
            val book = {
                bookingPolicyService.setEmployeePolicy(employeeId, roomTypes)
            }
            book()
            shouldThrow<EmployeePolicyDuplicated> {
                book()
            }
        }
    }

    "isBookingAllowed" - {

        "if no policy is added should return false" {
            bookingPolicyService.isBookingAllowed(employeeId, roomType) shouldBe false
        }

        "if employee policy is added should return true" {
            bookingPolicyService.setEmployeePolicy(employeeId, roomTypes)
            bookingPolicyService.isBookingAllowed(employeeId, roomType) shouldBe true
        }

        "if employee policy is added should return true only if is the employee" {
            bookingPolicyService.setEmployeePolicy(employeeId, roomTypes)
            val otherEmployeeId = EmployeeId("other employee")
            bookingPolicyService.isBookingAllowed(otherEmployeeId, roomType) shouldBe false
        }

        "can have several empoyeeId policy" {
            val otherEmployeeId = EmployeeId("other employee")

            bookingPolicyService.setEmployeePolicy(employeeId, roomTypes)
            bookingPolicyService.setEmployeePolicy(otherEmployeeId, roomTypes)

            bookingPolicyService.isBookingAllowed(employeeId, roomType) shouldBe true
            bookingPolicyService.isBookingAllowed(otherEmployeeId, roomType) shouldBe true
        }
    }

})