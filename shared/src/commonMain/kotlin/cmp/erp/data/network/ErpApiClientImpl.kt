package cmp.erp.data.network

import cmp.erp.domain.model.ApiResponse
import cmp.erp.domain.model.AuthToken
import cmp.erp.domain.model.Employee

class ErpApiClientImpl(
    private val baseUrl: String = "https://api.erp.local",
    private val tokenProvider: (() -> String?)? = null
) : ErpApiClient {
    override suspend fun login(email: String, password: String): ApiResponse<AuthToken> {
        // TODO: Implement actual API call
        return ApiResponse(success = false, message = "Not implemented")
    }

    override suspend fun refreshToken(refreshToken: String): ApiResponse<AuthToken> {
        // TODO: Implement actual API call
        return ApiResponse(success = false, message = "Not implemented")
    }

    override suspend fun logout(): ApiResponse<Unit> {
        // TODO: Implement actual API call
        return ApiResponse(success = false, message = "Not implemented")
    }

    override suspend fun getAllEmployees(): ApiResponse<List<Employee>> {
        // TODO: Implement actual API call
        return ApiResponse(success = false, message = "Not implemented")
    }

    override suspend fun getEmployeeById(employeeId: String): ApiResponse<Employee> {
        // TODO: Implement actual API call
        return ApiResponse(success = false, message = "Not implemented")
    }
}

