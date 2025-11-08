package cmp.erp.domain.usecase

import cmp.erp.domain.model.Employee
import cmp.erp.domain.model.Result
import cmp.erp.domain.repository.EmployeeRepository
import kotlinx.coroutines.flow.Flow

class GetAllEmployeesUseCase(private val employeeRepository: EmployeeRepository) {
    operator fun invoke(): Flow<Result<List<Employee>>> {
        return employeeRepository.getAllEmployees()
    }
}

class GetEmployeeByIdUseCase(private val employeeRepository: EmployeeRepository) {
    operator fun invoke(employeeId: String): Flow<Result<Employee>> {
        return employeeRepository.getEmployeeById(employeeId)
    }
}

class SearchEmployeesUseCase(private val employeeRepository: EmployeeRepository) {
    operator fun invoke(query: String): Flow<Result<List<Employee>>> {
        return employeeRepository.searchEmployees(query)
    }
}

class SyncEmployeesUseCase(private val employeeRepository: EmployeeRepository) {
    suspend operator fun invoke(): Result<Unit> {
        return employeeRepository.syncEmployees()
    }
}

