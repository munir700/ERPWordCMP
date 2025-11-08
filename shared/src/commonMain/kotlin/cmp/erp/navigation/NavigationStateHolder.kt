package cmp.erp.navigation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Navigation back stack entry
 */
data class NavigationBackStack(
    val route: String,
    val arguments: Map<String, String> = emptyMap()
)

/**
 * Navigation state holder for managing screen navigation
 */
open class NavigationStateHolder {
    private val _currentRoute = MutableStateFlow(NavigationRoute.Login.route)
    val currentRoute: StateFlow<String> = _currentRoute.asStateFlow()

    private val _backStack = MutableStateFlow<List<NavigationBackStack>>(emptyList())
    val backStack: StateFlow<List<NavigationBackStack>> = _backStack.asStateFlow()

    private val _canGoBack = MutableStateFlow(false)
    val canGoBack: StateFlow<Boolean> = _canGoBack.asStateFlow()

    /**
     * Navigate to a new screen
     */
    fun navigateTo(route: String, addToBackStack: Boolean = true) {
        if (addToBackStack && _currentRoute.value != route) {
            val newBackStack = _backStack.value.toMutableList()
            newBackStack.add(NavigationBackStack(_currentRoute.value))
            _backStack.value = newBackStack
        }
        _currentRoute.value = route
        updateCanGoBack()
    }

    /**
     * Navigate with arguments
     */
    fun navigateWithArgs(route: String, arguments: Map<String, String> = emptyMap(), addToBackStack: Boolean = true) {
        if (addToBackStack && _currentRoute.value != route) {
            val newBackStack = _backStack.value.toMutableList()
            newBackStack.add(NavigationBackStack(_currentRoute.value, arguments))
            _backStack.value = newBackStack
        }
        _currentRoute.value = route
        updateCanGoBack()
    }

    /**
     * Go back to previous screen
     */
    fun goBack() {
        val backStack = _backStack.value
        if (backStack.isNotEmpty()) {
            val previousEntry = backStack.last()
            val newBackStack = backStack.dropLast(1)
            _backStack.value = newBackStack
            _currentRoute.value = previousEntry.route
            updateCanGoBack()
        }
    }

    /**
     * Pop to specific route in back stack
     */
    fun popTo(route: String, inclusive: Boolean = false) {
        val backStack = _backStack.value
        val indexToKeep = backStack.indexOfLast { it.route == route }

        if (indexToKeep >= 0) {
            val newBackStack = if (inclusive) {
                backStack.take(indexToKeep)
            } else {
                backStack.take(indexToKeep + 1)
            }
            _backStack.value = newBackStack
            if (newBackStack.isNotEmpty()) {
                _currentRoute.value = newBackStack.last().route
            }
            updateCanGoBack()
        }
    }

    /**
     * Clear all back stack and go to home
     */
    fun clearAndNavigateTo(route: String) {
        _backStack.value = emptyList()
        _currentRoute.value = route
        updateCanGoBack()
    }

    /**
     * Reset navigation to login
     */
    fun resetToLogin() {
        clearAndNavigateTo(NavigationRoute.Login.route)
    }

    private fun updateCanGoBack() {
        _canGoBack.value = _backStack.value.isNotEmpty()
    }
}


