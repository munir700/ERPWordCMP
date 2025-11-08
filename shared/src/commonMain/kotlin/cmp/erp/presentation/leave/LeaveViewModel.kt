package cmp.erp.presentation.leave

import cmp.erp.base.BaseViewModel
import cmp.erp.base.UiEffect
import cmp.erp.base.UiEvent
import cmp.erp.base.UiState
import cmp.erp.domain.model.LeaveRequest
import cmp.erp.domain.model.Result
import cmp.erp.domain.usecase.GetEmployeeLeaveUseCase
import cmp.erp.domain.usecase.GetPendingLeavesUseCase
import cmp.erp.domain.usecase.SubmitLeaveRequestUseCase
import kotlinx.coroutines.launch

// State
data class LeaveUiState(
    val leaves: List<LeaveRequest> = emptyList(),
    val pendingLeaves: List<LeaveRequest> = emptyList(),
    val selectedLeave: LeaveRequest? = null,
    override val isLoading: Boolean = false,
    override val error: String? = null
) : UiState

// Events
sealed class LeaveEvent : UiEvent {
    object LoadLeaves : LeaveEvent()
    object LoadPendingLeaves : LeaveEvent()
    data class SubmitLeave(val leaveRequest: LeaveRequest) : LeaveEvent()
    data class SelectLeave(val leave: LeaveRequest) : LeaveEvent()
    object ClearError : LeaveEvent()
}

// Effects
sealed class LeaveEffect : UiEffect {
    data class LeaveSubmitted(val message: String) : LeaveEffect()
    data class Error(val message: String) : LeaveEffect()
}

/**
 * ViewModel for Leave Management
 */
class LeaveViewModel(
    private val getEmployeeLeaveUseCase: GetEmployeeLeaveUseCase,
    private val getPendingLeavesUseCase: GetPendingLeavesUseCase,
    private val submitLeaveRequestUseCase: SubmitLeaveRequestUseCase,
    private val employeeId: String
) : BaseViewModel<LeaveEvent, LeaveUiState, LeaveEffect>(
    initialState = LeaveUiState()
) {

    override fun handleEvent(event: LeaveEvent) {
        when (event) {
            LeaveEvent.LoadLeaves -> loadLeaves()
            LeaveEvent.LoadPendingLeaves -> loadPendingLeaves()
            is LeaveEvent.SubmitLeave -> submitLeave(event.leaveRequest)
            is LeaveEvent.SelectLeave -> selectLeave(event.leave)
            LeaveEvent.ClearError -> clearError()
        }
    }

    private fun loadLeaves() {
        coroutineScope?.launch {
            setState { it.copy(isLoading = true) }
            try {
                getEmployeeLeaveUseCase(employeeId).collect { result ->
                    when (result) {
                        is Result.Success -> {
                            setState {
                                it.copy(
                                    isLoading = false,
                                    leaves = result.data
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

    private fun loadPendingLeaves() {
        coroutineScope?.launch {
            setState { it.copy(isLoading = true) }
            try {
                getPendingLeavesUseCase(employeeId).collect { result ->
                    when (result) {
                        is Result.Success -> {
                            setState {
                                it.copy(
                                    isLoading = false,
                                    pendingLeaves = result.data
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

    private fun submitLeave(leaveRequest: LeaveRequest) {
        coroutineScope?.launch {
            setState { it.copy(isLoading = true, error = null) }
            try {
                val result = submitLeaveRequestUseCase(leaveRequest)
                when (result) {
                    is Result.Success -> {
                        setState {
                            it.copy(
                                isLoading = false,
                                leaves = it.leaves + result.data
                            )
                        }
                        sendEffect(LeaveEffect.LeaveSubmitted("Leave request submitted successfully"))
                    }
                    is Result.Error -> {
                        setState { it.copy(isLoading = false, error = result.exception.message) }
                        sendEffect(LeaveEffect.Error(result.exception.message ?: "Failed to submit leave"))
                    }
                    Result.Loading -> {}
                }
            } catch (e: Exception) {
                setState { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    private fun selectLeave(leave: LeaveRequest) {
        setState { it.copy(selectedLeave = leave) }
    }

    private fun clearError() {
        setState { it.copy(error = null) }
    }
}

