package hotelbooking

import hotelbooking.model.CompanyId
import hotelbooking.model.EmployeeId

interface Belongable {
    fun company(employeeId: EmployeeId): CompanyId?
}
