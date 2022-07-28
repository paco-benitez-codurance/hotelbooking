package hotelbooking

import hotelbooking.model.CompanyId
import hotelbooking.model.EmployeeId
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.assertThrows

class CompanyServiceTest : FreeSpec({

    val companyId = CompanyId("company id")
    val employeeId = EmployeeId("employee id")
    val otherCompanyId = CompanyId("other company id")
    val otherEmployeeId = EmployeeId("other employee id")

    lateinit var companyService: CompanyService
    lateinit var belongable: Belongable
    val deleteIsDone: (EmployeeId) -> Unit = mockk()

    beforeEach {
        companyService = CompanyService(deleteIsDone)
        belongable = companyService
        every { deleteIsDone(any()) } returns Unit
    }

    "Add Employee" - {

        "Employee should not be duplicated" {
            companyService.addEmployee(companyId, employeeId)
            assertThrows<DuplicatedEmployee> {
                companyService.addEmployee(companyId, employeeId)
            }
        }

        "After add employee, company is known" {
            companyService.addEmployee(companyId, employeeId)
            belongable.company(employeeId) shouldBe companyId
        }

        "Should work for two employees" {
            companyService.addEmployee(companyId, employeeId)
            companyService.addEmployee(otherCompanyId, otherEmployeeId)
            belongable.company(employeeId) shouldBe companyId
        }
    }

    "Delete employee" - {
        "should remove employee from the system" {
            companyService.addEmployee(companyId, employeeId)
            companyService.deleteEmployee(employeeId)
            belongable.company(employeeId) shouldBe null
        }

        "should inform that employeeId is remove" {
            companyService.addEmployee(companyId, employeeId)
            companyService.deleteEmployee(employeeId)

            verify { deleteIsDone(employeeId) }
        }
    }


})
