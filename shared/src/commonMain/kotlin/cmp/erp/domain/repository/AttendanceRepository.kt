package cmp.erp.domain.repository

import cmp.erp.domain.model.AttendanceRecord
import cmp.erp.domain.model.Location
import cmp.erp.domain.model.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

data class AttendanceStats(
    val presentCount: Int = 0,
    val absentCount: Int = 0,
    val halfDayCount: Int = 0,
    val onLeaveCount: Int = 0,
    val totalWorkingHours: Double = 0.0
)

interface AttendanceRepository {
    suspend fun checkIn(employeeId: String, location: Location): Result<AttendanceRecord>
    suspend fun checkOut(employeeId: String, location: Location): Result<AttendanceRecord>
    fun getTodayAttendance(employeeId: String): Flow<Result<AttendanceRecord?>>
    fun getAttendanceRecords(employeeId: String, startDate: LocalDate, endDate: LocalDate): Flow<Result<List<AttendanceRecord>>>
    fun getAttendanceStats(employeeId: String, month: Int, year: Int): Flow<Result<AttendanceStats>>
    suspend fun syncOfflineAttendance(): Result<Unit>
}

