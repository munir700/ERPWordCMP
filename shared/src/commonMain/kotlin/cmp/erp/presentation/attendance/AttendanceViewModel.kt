package cmp.erp.presentation.attendance

import cmp.erp.base.BaseViewModel
import cmp.erp.base.UiEffect
import cmp.erp.base.UiEvent
import cmp.erp.base.UiState
import cmp.erp.domain.model.AttendanceRecord
import cmp.erp.domain.usecase.CheckInUseCase
import cmp.erp.domain.usecase.CheckOutUseCase
import cmp.erp.domain.usecase.GetTodayAttendanceUseCase
import cmp.erp.domain.model.Location
import cmp.erp.domain.model.Result
import kotlinx.coroutines.launch

// State
data class AttendanceUiState(
    val currentAttendance: AttendanceRecord? = null,
    val isCheckedIn: Boolean = false,
    val checkedInTime: String? = null,
    val checkedOutTime: String? = null,
    override val isLoading: Boolean = false,
    override val error: String? = null
) : UiState

// Events
sealed class AttendanceEvent : UiEvent {
    data class CheckIn(val location: Location) : AttendanceEvent()
    data class CheckOut(val location: Location) : AttendanceEvent()
    object LoadTodayAttendance : AttendanceEvent()
    object ClearError : AttendanceEvent()
}

// Effects
sealed class AttendanceEffect : UiEffect {
    data class CheckInSuccess(val message: String) : AttendanceEffect()
    data class CheckOutSuccess(val message: String) : AttendanceEffect()
    data class Error(val message: String) : AttendanceEffect()
}

/**
 * ViewModel for Attendance check-in/out
 */
class AttendanceViewModel(
    private val checkInUseCase: CheckInUseCase,
    private val checkOutUseCase: CheckOutUseCase,
    private val getTodayAttendanceUseCase: GetTodayAttendanceUseCase,
    private val employeeId: String
) : BaseViewModel<AttendanceEvent, AttendanceUiState, AttendanceEffect>(
    initialState = AttendanceUiState()
) {

    override fun handleEvent(event: AttendanceEvent) {
        when (event) {
            is AttendanceEvent.CheckIn -> checkIn(event.location)
            is AttendanceEvent.CheckOut -> checkOut(event.location)
            AttendanceEvent.LoadTodayAttendance -> loadTodayAttendance()
            AttendanceEvent.ClearError -> clearError()
        }
    }

    private fun checkIn(location: Location) {
        coroutineScope?.launch {
            setState { it.copy(isLoading = true, error = null) }
            try {
                val result = checkInUseCase(employeeId, location)
                when (result) {
                    is Result.Success -> {
                        setState {
                            it.copy(
                                isLoading = false,
                                isCheckedIn = true,
                                currentAttendance = result.data,
                                checkedInTime = result.data.checkInTime.toString()
                            )
                        }
                        sendEffect(AttendanceEffect.CheckInSuccess("Checked in successfully"))
                    }
                    is Result.Error -> {
                        setState { it.copy(isLoading = false, error = result.exception.message) }
                        sendEffect(AttendanceEffect.Error(result.exception.message ?: "Check-in failed"))
                    }
                    Result.Loading -> {}
                }
            } catch (e: Exception) {
                setState { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    private fun checkOut(location: Location) {
        coroutineScope?.launch {
            setState { it.copy(isLoading = true, error = null) }
            try {
                val result = checkOutUseCase(employeeId, location)
                when (result) {
                    is Result.Success -> {
                        setState {
                            it.copy(
                                isLoading = false,
                                isCheckedIn = false,
                                currentAttendance = result.data,
                                checkedOutTime = result.data.checkOutTime?.toString()
                            )
                        }
                        sendEffect(AttendanceEffect.CheckOutSuccess("Checked out successfully"))
                    }
                    is Result.Error -> {
                        setState { it.copy(isLoading = false, error = result.exception.message) }
                        sendEffect(AttendanceEffect.Error(result.exception.message ?: "Check-out failed"))
                    }
                    Result.Loading -> {}
                }
            } catch (e: Exception) {
                setState { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    private fun loadTodayAttendance() {
        coroutineScope?.launch {
            setState { it.copy(isLoading = true) }
            try {
                getTodayAttendanceUseCase(employeeId).collect { result ->
                    when (result) {
                        is Result.Success -> {
                            val attendance = result.data
                            setState {
                                it.copy(
                                    isLoading = false,
                                    currentAttendance = attendance,
                                    isCheckedIn = attendance != null && attendance.checkOutTime == null,
                                    checkedInTime = attendance?.checkInTime?.toString(),
                                    checkedOutTime = attendance?.checkOutTime?.toString()
                                )
                            }
                        }
                        is Result.Error -> {
                            setState { it.copy(isLoading = false, error = result.exception.message) }
                        }
                        Result.Loading -> setState { it.copy(isLoading = true) }
                    }
                }
            } catch (e: Exception) {
                setState { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    private fun clearError() {
        setState { it.copy(error = null) }
    }
}

