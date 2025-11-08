package cmp.erp.presentation.payroll

import cmp.erp.base.BaseViewModel
import cmp.erp.base.UiEffect
import cmp.erp.base.UiEvent
import cmp.erp.base.UiState
import cmp.erp.domain.model.Payroll
import cmp.erp.domain.model.Result
import cmp.erp.domain.usecase.GetEmployeePayrollUseCase
import cmp.erp.domain.usecase.GetPayrollByMonthYearUseCase
import kotlinx.coroutines.launch

// State
data class PayrollUiState(
    val payrolls: List<Payroll> = emptyList(),
    val selectedPayroll: Payroll? = null,
    val currentMonthPayroll: Payroll? = null,
    override val isLoading: Boolean = false,
    override val error: String? = null
) : UiState

// Events
sealed class PayrollEvent : UiEvent {
    object LoadPayrolls : PayrollEvent()
    data class LoadPayrollByMonthYear(val month: Int, val year: Int) : PayrollEvent()
    data class SelectPayroll(val payroll: Payroll) : PayrollEvent()
    object ClearError : PayrollEvent()
}

// Effects
sealed class PayrollEffect : UiEffect {
    data class Error(val message: String) : PayrollEffect()
}

/**
 * ViewModel for Payroll Management
 */
class PayrollViewModel(
    private val getEmployeePayrollUseCase: GetEmployeePayrollUseCase,
    private val getPayrollByMonthYearUseCase: GetPayrollByMonthYearUseCase,
    private val employeeId: String
) : BaseViewModel<PayrollEvent, PayrollUiState, PayrollEffect>(
    initialState = PayrollUiState()
) {

    override fun handleEvent(event: PayrollEvent) {
        when (event) {
            PayrollEvent.LoadPayrolls -> loadPayrolls()
            is PayrollEvent.LoadPayrollByMonthYear -> loadPayrollByMonthYear(event.month, event.year)
            is PayrollEvent.SelectPayroll -> selectPayroll(event.payroll)
            PayrollEvent.ClearError -> clearError()
        }
    }

    private fun loadPayrolls() {
        coroutineScope?.launch {
            setState { it.copy(isLoading = true) }
            try {
                getEmployeePayrollUseCase(employeeId).collect { result ->
                    when (result) {
                        is Result.Success -> {
                            setState {
                                it.copy(
                                    isLoading = false,
                                    payrolls = result.data
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

    private fun loadPayrollByMonthYear(month: Int, year: Int) {
        coroutineScope?.launch {
            setState { it.copy(isLoading = true) }
            try {
                getPayrollByMonthYearUseCase(employeeId, month, year).collect { result ->
                    when (result) {
                        is Result.Success -> {
                            setState {
                                it.copy(
                                    isLoading = false,
                                    currentMonthPayroll = result.data
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

    private fun selectPayroll(payroll: Payroll) {
        setState { it.copy(selectedPayroll = payroll) }
    }

    private fun clearError() {
        setState { it.copy(error = null) }
    }
}

