package cmp.erp.presentation.employee

import cmp.erp.base.BaseViewModel
import cmp.erp.base.UiEffect
import cmp.erp.base.UiEvent
import cmp.erp.base.UiState
import cmp.erp.domain.model.Employee
import cmp.erp.domain.model.Result
import cmp.erp.domain.usecase.GetAllEmployeesUseCase
import cmp.erp.domain.usecase.GetEmployeeByIdUseCase
import cmp.erp.domain.usecase.SearchEmployeesUseCase
import cmp.erp.domain.usecase.SyncEmployeesUseCase
import kotlinx.coroutines.launch

// State
data class EmployeeUiState(
    val employees: List<Employee> = emptyList(),
    val selectedEmployee: Employee? = null,
    val searchQuery: String = "",
    val searchResults: List<Employee> = emptyList(),
    val isSyncing: Boolean = false,
    override val isLoading: Boolean = false,
    override val error: String? = null
) : UiState

// Events
sealed class EmployeeEvent : UiEvent {
    object LoadAllEmployees : EmployeeEvent()
    data class GetEmployeeById(val employeeId: String) : EmployeeEvent()
    data class SearchEmployees(val query: String) : EmployeeEvent()
    object SyncEmployees : EmployeeEvent()
    data class SelectEmployee(val employee: Employee) : EmployeeEvent()
    object ClearError : EmployeeEvent()
}

// Effects
sealed class EmployeeEffect : UiEffect {
    data class SyncCompleted(val message: String) : EmployeeEffect()
    data class Error(val message: String) : EmployeeEffect()
}

/**
 * ViewModel for Employee Management
 */
class EmployeeViewModel(
    private val getAllEmployeesUseCase: GetAllEmployeesUseCase,
    private val getEmployeeByIdUseCase: GetEmployeeByIdUseCase,
    private val searchEmployeesUseCase: SearchEmployeesUseCase,
    private val syncEmployeesUseCase: SyncEmployeesUseCase
) : BaseViewModel<EmployeeEvent, EmployeeUiState, EmployeeEffect>(
    initialState = EmployeeUiState()
) {

    override fun handleEvent(event: EmployeeEvent) {
        when (event) {
            EmployeeEvent.LoadAllEmployees -> loadAllEmployees()
            is EmployeeEvent.GetEmployeeById -> getEmployeeById(event.employeeId)
            is EmployeeEvent.SearchEmployees -> searchEmployees(event.query)
            EmployeeEvent.SyncEmployees -> syncEmployees()
            is EmployeeEvent.SelectEmployee -> selectEmployee(event.employee)
            EmployeeEvent.ClearError -> clearError()
        }
    }

    private fun loadAllEmployees() {
        coroutineScope?.launch {
            setState { it.copy(isLoading = true) }
            try {
                getAllEmployeesUseCase().collect { result ->
                    when (result) {
                        is Result.Success -> {
                            setState {
                                it.copy(
                                    isLoading = false,
                                    employees = result.data
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

    private fun getEmployeeById(employeeId: String) {
        coroutineScope?.launch {
            setState { it.copy(isLoading = true) }
            try {
                getEmployeeByIdUseCase(employeeId).collect { result ->
                    when (result) {
                        is Result.Success -> {
                            setState {
                                it.copy(
                                    isLoading = false,
                                    selectedEmployee = result.data
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

    private fun searchEmployees(query: String) {
        coroutineScope?.launch {
            setState { it.copy(searchQuery = query, isLoading = true) }
            try {
                searchEmployeesUseCase(query).collect { result ->
                    when (result) {
                        is Result.Success -> {
                            setState {
                                it.copy(
                                    isLoading = false,
                                    searchResults = result.data
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

    private fun syncEmployees() {
        coroutineScope?.launch {
            setState { it.copy(isSyncing = true) }
            try {
                val result = syncEmployeesUseCase()
                when (result) {
                    is Result.Success -> {
                        setState { it.copy(isSyncing = false) }
                        sendEffect(EmployeeEffect.SyncCompleted("Employees synced successfully"))
                        // Reload employees after sync
                        loadAllEmployees()
                    }
                    is Result.Error -> {
                        setState { it.copy(isSyncing = false, error = result.exception.message) }
                        sendEffect(EmployeeEffect.Error(result.exception.message ?: "Sync failed"))
                    }
                    Result.Loading -> {}
                }
            } catch (e: Exception) {
                setState { it.copy(isSyncing = false, error = e.message) }
            }
        }
    }

    private fun selectEmployee(employee: Employee) {
        setState { it.copy(selectedEmployee = employee) }
    }

    private fun clearError() {
        setState { it.copy(error = null) }
    }
}

