package cmp.erp.domain.usecase

import cmp.erp.domain.model.LeaveRequest
import cmp.erp.domain.model.Result
import cmp.erp.domain.repository.LeaveBalance
import cmp.erp.domain.repository.LeaveRepository
import kotlinx.coroutines.flow.Flow

class GetEmployeeLeaveUseCase(private val leaveRepository: LeaveRepository) {
    operator fun invoke(employeeId: String): Flow<Result<List<LeaveRequest>>> {
        return leaveRepository.getEmployeeLeaves(employeeId)
    }
}

class GetPendingLeavesUseCase(private val leaveRepository: LeaveRepository) {
    operator fun invoke(employeeId: String): Flow<Result<List<LeaveRequest>>> {
        return leaveRepository.getPendingLeaves(employeeId)
    }
}

class SubmitLeaveRequestUseCase(private val leaveRepository: LeaveRepository) {
    suspend operator fun invoke(leaveRequest: LeaveRequest): Result<LeaveRequest> {
        return leaveRepository.submitLeaveRequest(leaveRequest)
    }
}

class ApproveLeaveUseCase(private val leaveRepository: LeaveRepository) {
    suspend operator fun invoke(leaveId: String, approvedBy: String): Result<LeaveRequest> {
        return leaveRepository.approveLeave(leaveId, approvedBy)
    }
}

class RejectLeaveUseCase(private val leaveRepository: LeaveRepository) {
    suspend operator fun invoke(leaveId: String, rejectionReason: String): Result<LeaveRequest> {
        return leaveRepository.rejectLeave(leaveId, rejectionReason)
    }
}

class GetLeaveBalanceUseCase(private val leaveRepository: LeaveRepository) {
    operator fun invoke(employeeId: String): Flow<Result<LeaveBalance>> {
        return leaveRepository.getLeaveBalance(employeeId)
    }
}

