package cmp.erp.data.repository

import cmp.erp.data.local.LocalDataSource
import cmp.erp.data.network.ErpApiClient
import cmp.erp.domain.model.LeaveRequest
import cmp.erp.domain.model.LeaveStatus
import cmp.erp.domain.model.LeaveType
import cmp.erp.domain.model.Result
import cmp.erp.domain.repository.LeaveBalance
import cmp.erp.domain.repository.LeaveRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.LocalDate

class LeaveRepositoryImpl(
    private val apiClient: ErpApiClient,
    private val localDataSource: LocalDataSource
) : LeaveRepository {

    override fun getEmployeeLeaves(employeeId: String): Flow<Result<List<LeaveRequest>>> = flow {
        emit(Result.Loading)
        try {
            emit(Result.Success(emptyList()))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    override fun getPendingLeaves(employeeId: String): Flow<Result<List<LeaveRequest>>> = flow {
        emit(Result.Loading)
        try {
            emit(Result.Success(emptyList()))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    override suspend fun submitLeaveRequest(leaveRequest: LeaveRequest): Result<LeaveRequest> {
        return try {
            Result.Success(leaveRequest)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun approveLeave(leaveId: String, approvedBy: String): Result<LeaveRequest> {
        return try {
            val leave = LeaveRequest(
                id = leaveId,
                employeeId = "",
                leaveType = LeaveType.CASUAL,
                startDate = LocalDate(2025, 11, 8),
                endDate = LocalDate(2025, 11, 8),
                numberOfDays = 1.0,
                reason = "",
                status = LeaveStatus.APPROVED
            )
            Result.Success(leave)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun rejectLeave(leaveId: String, rejectionReason: String): Result<LeaveRequest> {
        return try {
            val leave = LeaveRequest(
                id = leaveId,
                employeeId = "",
                leaveType = LeaveType.CASUAL,
                startDate = LocalDate(2025, 11, 8),
                endDate = LocalDate(2025, 11, 8),
                numberOfDays = 1.0,
                reason = "",
                status = LeaveStatus.REJECTED
            )
            Result.Success(leave)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override fun getLeaveBalance(employeeId: String): Flow<Result<LeaveBalance>> = flow {
        emit(Result.Loading)
        try {
            emit(Result.Success(LeaveBalance()))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }
}
