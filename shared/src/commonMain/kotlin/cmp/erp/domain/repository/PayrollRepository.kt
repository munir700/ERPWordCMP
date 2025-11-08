package cmp.erp.domain.repository

import cmp.erp.domain.model.Payroll
import cmp.erp.domain.model.Result
import kotlinx.coroutines.flow.Flow

interface PayrollRepository {
    fun getEmployeePayroll(employeeId: String): Flow<Result<List<Payroll>>>
    fun getPayrollByMonthYear(employeeId: String, month: Int, year: Int): Flow<Result<Payroll?>>
    suspend fun syncPayroll(): Result<Unit>
    suspend fun processPayroll(employeeIds: List<String>, month: Int, year: Int): Result<List<Payroll>>
}

