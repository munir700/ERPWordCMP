package cmp.erp.data.repository

import cmp.erp.data.local.LocalDataSource
import cmp.erp.data.network.ErpApiClient
import cmp.erp.domain.model.Employee
import cmp.erp.domain.model.Result
import cmp.erp.domain.repository.EmployeeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach

class EmployeeRepositoryImpl(
    private val apiClient: ErpApiClient,
    private val localDataSource: LocalDataSource
) : EmployeeRepository {

    override fun getAllEmployees(): Flow<Result<List<Employee>>> = flow {
        emit(Result.Loading)
        try {
            // Get from local first
            localDataSource.getAllEmployees().collect { local ->
                emit(Result.Success(local))
            }
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    override fun getEmployeeById(employeeId: String): Flow<Result<Employee>> = flow {
        emit(Result.Loading)
        try {
            val local = localDataSource.getEmployee(employeeId)
            if (local != null) {
                emit(Result.Success(local))
            } else {
                emit(Result.Error(Exception("Employee not found")))
            }
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    override fun searchEmployees(query: String): Flow<Result<List<Employee>>> = flow {
        emit(Result.Loading)
        try {
            // Local search
            localDataSource.searchEmployees(query).collect { results ->
                emit(Result.Success(results))
            }
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    override suspend fun syncEmployees(): Result<Unit> {
        return try {
            val response = apiClient.getAllEmployees()
            if (response.success && response.data != null) {
                localDataSource.insertEmployees(response.data)
                Result.Success(Unit)
            } else {
                Result.Error(Exception(response.message ?: "Sync failed"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}
