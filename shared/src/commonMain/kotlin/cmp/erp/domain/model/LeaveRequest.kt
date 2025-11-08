package cmp.erp.domain.model

import kotlinx.serialization.Serializable
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalDate

/**
 * Leave request entity for managing employee leave
 */
@Serializable
data class LeaveRequest(
    val id: String,
    val employeeId: String,
    val leaveType: LeaveType,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val numberOfDays: Double,
    val reason: String?,
    val status: LeaveStatus = LeaveStatus.PENDING,
    val approvedBy: String? = null,
    val rejectionReason: String? = null,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null
)

@Serializable
enum class LeaveType {
    CASUAL, SICK, EARNED, UNPAID, MATERNITY, PATERNITY
}

@Serializable
enum class LeaveStatus {
    PENDING, APPROVED, REJECTED, CANCELLED
}

