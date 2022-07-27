package hotelbooking

import hotelbooking.model.CompanyId
import hotelbooking.model.EmployeeId
import hotelbooking.model.RoomType
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



    "if no policy is added should return true" {
        bookingPolicyService.isBookingAllowed(employeeId, roomType) shouldBe true
    }

    "isBookingAllowed with employee policy" - {

        "if employee policy is added should return true" {
            bookingPolicyService.setEmployeePolicy(employeeId, roomTypes)
            bookingPolicyService.isBookingAllowed(employeeId, roomType) shouldBe true
        }

        "if employee policy is added and ask for other room type should be false" {
            bookingPolicyService.setEmployeePolicy(employeeId, roomTypes)
            bookingPolicyService.isBookingAllowed(employeeId, RoomType("another")) shouldBe false
        }

        "if employee policy for other employee should be true" {
            bookingPolicyService.setEmployeePolicy(employeeId, roomTypes)
            val otherEmployeeId = EmployeeId("other employee")
            bookingPolicyService.isBookingAllowed(otherEmployeeId, roomType) shouldBe true
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

        "if company policy is added should return true for an employee that does not belong to the company" {
            bookingPolicyService.setCompanyPolicy(companyId, roomTypes)
            bookingPolicyService.isBookingAllowed(employeeId, roomType) shouldBe true
        }

        "if company policy is added should return true for an employee that belongs to the company" {
            bookingPolicyService.setCompanyPolicy(companyId, roomTypes)
            every { belongable.company(employeeId) } returns companyId
            bookingPolicyService.isBookingAllowed(employeeId, roomType) shouldBe true
        }

        "if company policy is added should return true for an employee that does not belong to the company" {
            bookingPolicyService.setCompanyPolicy(companyId, roomTypes)
            val anotherCompanyId = CompanyId("Another company id")
            every { belongable.company(employeeId) } returns anotherCompanyId
            bookingPolicyService.isBookingAllowed(employeeId, roomType) shouldBe true
        }
    }

    "isBookingAllowed employee and booking policies" - {
        "Employee policy should take preference" {
            val roomTypeForCompany = RoomType("forCompany")
            val roomTypesForCompany = arrayListOf(roomTypeForCompany)
            val roomTypesForEmployees = arrayListOf(RoomType("forEmployee"))

            bookingPolicyService.setCompanyPolicy(companyId, roomTypesForCompany)
            bookingPolicyService.setEmployeePolicy(employeeId, roomTypesForEmployees)

            every { belongable.company(employeeId) } returns companyId

            bookingPolicyService.isBookingAllowed(employeeId, roomTypeForCompany) shouldBe false
        }

        "If no rules should be allowed to check any room type" {
            bookingPolicyService.isBookingAllowed(employeeId, roomType) shouldBe true
        }
    }
})