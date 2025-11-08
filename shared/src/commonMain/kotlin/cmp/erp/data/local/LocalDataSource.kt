package cmp.erp.data.local

import cmp.erp.domain.model.Employee
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    fun getAllEmployees(): Flow<List<Employee>>
    suspend fun insertEmployees(employees: List<Employee>)
    suspend fun getEmployee(employeeId: String): Employee?
    suspend fun insertEmployee(employee: Employee)
    fun searchEmployees(query: String): Flow<List<Employee>>
}

