package cmp.erp.domain.usecase

import cmp.erp.domain.model.Payroll
import cmp.erp.domain.model.Result
import cmp.erp.domain.repository.PayrollRepository
import kotlinx.coroutines.flow.Flow

class GetEmployeePayrollUseCase(private val payrollRepository: PayrollRepository) {
    operator fun invoke(employeeId: String): Flow<Result<List<Payroll>>> {
        return payrollRepository.getEmployeePayroll(employeeId)
    }
}

class GetPayrollByMonthYearUseCase(private val payrollRepository: PayrollRepository) {
    operator fun invoke(
        employeeId: String,
        month: Int,
        year: Int
    ): Flow<Result<Payroll?>> {
        return payrollRepository.getPayrollByMonthYear(employeeId, month, year)
    }
}

class SyncPayrollUseCase(private val payrollRepository: PayrollRepository) {
    suspend operator fun invoke(): Result<Unit> {
        return payrollRepository.syncPayroll()
    }
}

class ProcessPayrollUseCase(private val payrollRepository: PayrollRepository) {
    suspend operator fun invoke(
        employeeIds: List<String>,
        month: Int,
        year: Int
    ): Result<List<Payroll>> {
        return payrollRepository.processPayroll(employeeIds, month, year)
    }
}

