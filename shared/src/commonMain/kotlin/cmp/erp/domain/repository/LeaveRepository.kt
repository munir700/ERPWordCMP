package cmp.erp.domain.repository

import cmp.erp.domain.model.LeaveRequest
import cmp.erp.domain.model.Result
import kotlinx.coroutines.flow.Flow

data class LeaveBalance(
    val casualLeaves: Int = 0,
    val sickLeaves: Int = 0,
    val earnedLeaves: Int = 0,
    val unpaidLeaves: Int = 0,
    val maternityLeaves: Int = 0,
    val paternityLeaves: Int = 0
)

interface LeaveRepository {
    fun getEmployeeLeaves(employeeId: String): Flow<Result<List<LeaveRequest>>>
    fun getPendingLeaves(employeeId: String): Flow<Result<List<LeaveRequest>>>
    suspend fun submitLeaveRequest(leaveRequest: LeaveRequest): Result<LeaveRequest>
    suspend fun approveLeave(leaveId: String, approvedBy: String): Result<LeaveRequest>
    suspend fun rejectLeave(leaveId: String, rejectionReason: String): Result<LeaveRequest>
    fun getLeaveBalance(employeeId: String): Flow<Result<LeaveBalance>>
}

