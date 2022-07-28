package hotelbooking

import hotelbooking.model.CompanyId
import hotelbooking.model.EmployeeId
import io.kotest.core.spec.style.FunSpec
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.assertThrows

class CompanyServiceTest : StringSpec ({

    val companyId = CompanyId("company id")
    val employeeId = EmployeeId("employee id")
    val otherCompanyId = CompanyId("other company id")
    val otherEmployeeId = EmployeeId("other employee id")

    lateinit var companyService: CompanyService
    lateinit var belongable: Belongable

    beforeEach{
        companyService = CompanyService()
        belongable = companyService
    }

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


})
