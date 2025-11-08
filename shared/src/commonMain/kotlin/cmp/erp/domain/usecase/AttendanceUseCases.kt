package cmp.erp.domain.usecase

import cmp.erp.domain.model.AttendanceRecord
import cmp.erp.domain.model.Location
import cmp.erp.domain.model.Result
import cmp.erp.domain.repository.AttendanceRepository
import cmp.erp.domain.repository.AttendanceStats
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

class CheckInUseCase(private val attendanceRepository: AttendanceRepository) {
    suspend operator fun invoke(employeeId: String, location: Location): Result<AttendanceRecord> {
        return attendanceRepository.checkIn(employeeId, location)
    }
}

class CheckOutUseCase(private val attendanceRepository: AttendanceRepository) {
    suspend operator fun invoke(employeeId: String, location: Location): Result<AttendanceRecord> {
        return attendanceRepository.checkOut(employeeId, location)
    }
}

class GetTodayAttendanceUseCase(private val attendanceRepository: AttendanceRepository) {
    operator fun invoke(employeeId: String): Flow<Result<AttendanceRecord?>> {
        return attendanceRepository.getTodayAttendance(employeeId)
    }
}

class GetAttendanceRecordsUseCase(private val attendanceRepository: AttendanceRepository) {
    operator fun invoke(
        employeeId: String,
        startDate: LocalDate,
        endDate: LocalDate
    ): Flow<Result<List<AttendanceRecord>>> {
        return attendanceRepository.getAttendanceRecords(employeeId, startDate, endDate)
    }
}

class GetAttendanceStatsUseCase(private val attendanceRepository: AttendanceRepository) {
    operator fun invoke(
        employeeId: String,
        month: Int,
        year: Int
    ): Flow<Result<AttendanceStats>> {
        return attendanceRepository.getAttendanceStats(employeeId, month, year)
    }
}

class SyncOfflineAttendanceUseCase(private val attendanceRepository: AttendanceRepository) {
    suspend operator fun invoke(): Result<Unit> {
        return attendanceRepository.syncOfflineAttendance()
    }
}

