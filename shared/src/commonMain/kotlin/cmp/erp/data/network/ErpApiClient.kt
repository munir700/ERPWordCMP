package cmp.erp.data.network

import cmp.erp.domain.model.ApiResponse
import cmp.erp.domain.model.AuthToken
import cmp.erp.domain.model.Employee

interface ErpApiClient {
    suspend fun login(email: String, password: String): ApiResponse<AuthToken>
    suspend fun refreshToken(refreshToken: String): ApiResponse<AuthToken>
    suspend fun logout(): ApiResponse<Unit>
    suspend fun getAllEmployees(): ApiResponse<List<Employee>>
    suspend fun getEmployeeById(employeeId: String): ApiResponse<Employee>
}

