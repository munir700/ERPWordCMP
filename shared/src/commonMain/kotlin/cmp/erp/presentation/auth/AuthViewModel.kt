package cmp.erp.presentation.auth

import cmp.erp.base.BaseViewModel
import cmp.erp.base.UiEffect
import cmp.erp.base.UiEvent
import cmp.erp.base.UiState
import cmp.erp.domain.model.Result
import cmp.erp.domain.usecase.IsAuthenticatedUseCase
import cmp.erp.domain.usecase.LoginUseCase
import cmp.erp.domain.usecase.LogoutUseCase
import kotlinx.coroutines.launch

// State
data class AuthUiState(
    val isAuthenticated: Boolean = false,
    val currentEmail: String? = null,
    override val isLoading: Boolean = false,
    override val error: String? = null
) : UiState

// Events
sealed class AuthEvent : UiEvent {
    data class Login(val email: String, val password: String) : AuthEvent()
    object Logout : AuthEvent()
    object CheckAuthentication : AuthEvent()
    object ClearError : AuthEvent()
}

// Effects
sealed class AuthEffect : UiEffect {
    data class LoginSuccess(val message: String) : AuthEffect()
    data class LogoutSuccess(val message: String) : AuthEffect()
    data class Error(val message: String) : AuthEffect()
}

/**
 * ViewModel for Authentication
 */
class AuthViewModel(
    private val loginUseCase: LoginUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val isAuthenticatedUseCase: IsAuthenticatedUseCase
) : BaseViewModel<AuthEvent, AuthUiState, AuthEffect>(
    initialState = AuthUiState()
) {

    override fun handleEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.Login -> login(event.email, event.password)
            AuthEvent.Logout -> logout()
            AuthEvent.CheckAuthentication -> checkAuthentication()
            AuthEvent.ClearError -> clearError()
        }
    }

    private fun login(email: String, password: String) {
        coroutineScope?.launch {
            setState { it.copy(isLoading = true, error = null) }
            try {
                val result = loginUseCase(email, password)
                when (result) {
                    is Result.Success -> {
                        setState {
                            it.copy(
                                isLoading = false,
                                isAuthenticated = true,
                                currentEmail = email
                            )
                        }
                        sendEffect(AuthEffect.LoginSuccess("Login successful"))
                    }
                    is Result.Error -> {
                        setState { it.copy(isLoading = false, error = result.exception.message) }
                        sendEffect(AuthEffect.Error(result.exception.message ?: "Login failed"))
                    }
                    Result.Loading -> {}
                }
            } catch (e: Exception) {
                setState { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    private fun logout() {
        coroutineScope?.launch {
            setState { it.copy(isLoading = true) }
            try {
                val result = logoutUseCase()
                when (result) {
                    is Result.Success -> {
                        setState {
                            it.copy(
                                isLoading = false,
                                isAuthenticated = false,
                                currentEmail = null
                            )
                        }
                        sendEffect(AuthEffect.LogoutSuccess("Logged out successfully"))
                    }
                    is Result.Error -> {
                        setState { it.copy(isLoading = false, error = result.exception.message) }
                    }
                    Result.Loading -> {}
                }
            } catch (e: Exception) {
                setState { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    private fun checkAuthentication() {
        val isAuthenticated = isAuthenticatedUseCase()
        setState { it.copy(isAuthenticated = isAuthenticated) }
    }

    private fun clearError() {
        setState { it.copy(error = null) }
    }
}

