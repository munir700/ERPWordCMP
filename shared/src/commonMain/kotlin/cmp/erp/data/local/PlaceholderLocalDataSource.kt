package cmp.erp.data.local

import cmp.erp.domain.model.Employee
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class PlaceholderLocalDataSource : LocalDataSource {
    private val employees = mutableListOf<Employee>()

    override fun getAllEmployees(): Flow<List<Employee>> {
        return flowOf(employees)
    }

    override suspend fun insertEmployees(employees: List<Employee>) {
        this.employees.clear()
        this.employees.addAll(employees)
    }

    override suspend fun getEmployee(employeeId: String): Employee? {
        return employees.find { it.id == employeeId }
    }

    override suspend fun insertEmployee(employee: Employee) {
        employees.removeAll { it.id == employee.id }
        employees.add(employee)
    }

    override fun searchEmployees(query: String): Flow<List<Employee>> {
        val results = employees.filter {
            it.name.contains(query, ignoreCase = true) ||
            it.email.contains(query, ignoreCase = true) ||
            it.department.contains(query, ignoreCase = true)
        }
        return flowOf(results)
    }
}

