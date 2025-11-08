package cmp.erp.domain.repository

import cmp.erp.domain.model.Employee
import cmp.erp.domain.model.Result
import kotlinx.coroutines.flow.Flow

interface EmployeeRepository {
    fun getAllEmployees(): Flow<Result<List<Employee>>>
    fun getEmployeeById(employeeId: String): Flow<Result<Employee>>
    fun searchEmployees(query: String): Flow<Result<List<Employee>>>
    suspend fun syncEmployees(): Result<Unit>
}

