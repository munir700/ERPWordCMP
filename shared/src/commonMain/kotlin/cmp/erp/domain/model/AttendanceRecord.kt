package cmp.erp.domain.model

import kotlinx.serialization.Serializable
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalDate

/**
 * Attendance record entity
 */
@Serializable
data class AttendanceRecord(
    val id: String,
    val employeeId: String,
    val checkInTime: LocalDateTime,
    val checkOutTime: LocalDateTime? = null,
    val checkInLocation: Location? = null,
    val checkOutLocation: Location? = null,
    val workingHours: Double? = null,
    val status: AttendanceStatus = AttendanceStatus.PRESENT,
    val remarks: String? = null,
    val attendanceDate: LocalDate,
    val isOfflineSynced: Boolean = false,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null
)

@Serializable
enum class AttendanceStatus {
    PRESENT, ABSENT, HALF_DAY, LATE, ON_LEAVE, PENDING
}

