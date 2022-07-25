package hotelbooking

import hotelbooking.errors.CompanyPolicyDuplicated
import hotelbooking.errors.EmployeePolicyDuplicated
import hotelbooking.model.CompanyId
import hotelbooking.model.EmployeeId
import hotelbooking.model.RoomType
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec

import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk


class BookingPolicyServiceTest : FreeSpec({

    val companyId = CompanyId("company id")
    val roomType = RoomType("roomType")
    val roomTypes = listOf(roomType)
    val employeeId = EmployeeId("employee id")

    lateinit var bookingPolicyService: BookingPolicyService
    lateinit var belongable: Belongable

    beforeEach {
        belongable = mockk()
        every { belongable.company(any()) } returns null
        bookingPolicyService = BookingPolicyService(belongable)
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

    "if no policy is added should return false" {
        bookingPolicyService.isBookingAllowed(employeeId, roomType) shouldBe false
    }

    "isBookingAllowed with employee policy" - {

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

        "only is booking allowed if is same room type" {
            bookingPolicyService.setEmployeePolicy(employeeId, roomTypes)
            val otherRoomType = RoomType("other room type")
            bookingPolicyService.isBookingAllowed(employeeId, otherRoomType) shouldBe false
        }
    }

    "isBookingAllowed with company policy" - {
        "if company policy is added should return false for an employee that does not belong to the company" {
            bookingPolicyService.setCompanyPolicy(companyId, roomTypes)
            bookingPolicyService.isBookingAllowed(employeeId, roomType) shouldBe false
        }

        "if company policy is added should return true for an employee that belongs to the company" {
            bookingPolicyService.setCompanyPolicy(companyId, roomTypes)
            every { belongable.company(employeeId) } returns companyId
            bookingPolicyService.isBookingAllowed(employeeId, roomType) shouldBe true
        }
    }
})