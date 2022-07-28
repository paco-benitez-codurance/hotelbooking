package hotelbooking

import hotelbooking.model.CompanyId
import hotelbooking.model.EmployeeId

class CompanyService(val deleteIsDone: (EmployeeId) -> Unit) : Belongable {

    private var repo: Map<EmployeeId, CompanyId> = emptyMap()

    fun addEmployee(companyId: CompanyId, employeeId: EmployeeId) {
        if(repo.containsKey(employeeId)) throw DuplicatedEmployee()
        repo = repo + Pair(employeeId, companyId)
    }

    fun deleteEmployee(employeeId: EmployeeId) {
        repo = repo - employeeId
        deleteIsDone(employeeId)
    }

    override fun company(employeeId: EmployeeId): CompanyId? {
        return repo[employeeId]
    }
}