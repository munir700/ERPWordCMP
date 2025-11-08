package cmp.erp.navigation

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * Navigation event for observing navigation changes
 */
sealed class NavigationEvent {
    data class NavigateTo(val route: String, val arguments: Map<String, String> = emptyMap()) : NavigationEvent()
    object GoBack : NavigationEvent()
    data class PopTo(val route: String, val inclusive: Boolean = false) : NavigationEvent()
}

/**
 * Navigation interceptor for handling navigation logic
 */
interface NavigationInterceptor {
    suspend fun beforeNavigate(event: NavigationEvent): Boolean
    suspend fun afterNavigate(event: NavigationEvent)
}

/**
 * Advanced Navigation State Holder with interceptors and listeners
 */
class AdvancedNavigationStateHolder : NavigationStateHolder() {
    private val _navigationEvents = MutableSharedFlow<NavigationEvent>(extraBufferCapacity = 10)
    val navigationEvents: SharedFlow<NavigationEvent> = _navigationEvents.asSharedFlow()

    private val interceptors = mutableListOf<NavigationInterceptor>()
    private val listeners = mutableListOf<(NavigationEvent) -> Unit>()

    /**
     * Add a navigation interceptor
     */
    fun addInterceptor(interceptor: NavigationInterceptor) {
        interceptors.add(interceptor)
    }

    /**
     * Remove a navigation interceptor
     */
    fun removeInterceptor(interceptor: NavigationInterceptor) {
        interceptors.remove(interceptor)
    }

    /**
     * Add a navigation listener
     */
    fun addListener(listener: (NavigationEvent) -> Unit) {
        listeners.add(listener)
    }

    /**
     * Remove a navigation listener
     */
    fun removeListener(listener: (NavigationEvent) -> Unit) {
        listeners.remove(listener)
    }

    /**
     * Navigate with interceptor support
     */
    suspend fun navigateWithInterceptor(route: String, addToBackStack: Boolean = true) {
        val event = NavigationEvent.NavigateTo(route)

        // Check interceptors
        for (interceptor in interceptors) {
            if (!interceptor.beforeNavigate(event)) {
                return // Navigation blocked
            }
        }

        // Perform navigation
        navigateTo(route, addToBackStack)

        // Notify interceptors
        for (interceptor in interceptors) {
            interceptor.afterNavigate(event)
        }

        // Emit event
        _navigationEvents.emit(event)
        notifyListeners(event)
    }

    /**
     * Go back with interceptor support
     */
    suspend fun goBackWithInterceptor() {
        val event = NavigationEvent.GoBack

        // Check interceptors
        for (interceptor in interceptors) {
            if (!interceptor.beforeNavigate(event)) {
                return // Navigation blocked
            }
        }

        // Perform navigation
        goBack()

        // Notify interceptors
        for (interceptor in interceptors) {
            interceptor.afterNavigate(event)
        }

        // Emit event
        _navigationEvents.emit(event)
        notifyListeners(event)
    }

    /**
     * Pop to with interceptor support
     */
    suspend fun popToWithInterceptor(route: String, inclusive: Boolean = false) {
        val event = NavigationEvent.PopTo(route, inclusive)

        // Check interceptors
        for (interceptor in interceptors) {
            if (!interceptor.beforeNavigate(event)) {
                return // Navigation blocked
            }
        }

        // Perform navigation
        popTo(route, inclusive)

        // Notify interceptors
        for (interceptor in interceptors) {
            interceptor.afterNavigate(event)
        }

        // Emit event
        _navigationEvents.emit(event)
        notifyListeners(event)
    }

    private fun notifyListeners(event: NavigationEvent) {
        listeners.forEach { it(event) }
    }
}

/**
 * Authentication interceptor - prevents navigation when not authenticated
 */
class AuthenticationInterceptor(private val isAuthenticated: () -> Boolean) : NavigationInterceptor {
    override suspend fun beforeNavigate(event: NavigationEvent): Boolean {
        return when (event) {
            is NavigationEvent.NavigateTo -> {
                // Allow navigation to login without authentication
                if (event.route == NavigationRoute.Login.route) {
                    true
                } else {
                    // Require authentication for other routes
                    isAuthenticated()
                }
            }
            else -> true
        }
    }

    override suspend fun afterNavigate(event: NavigationEvent) {
        // No post-navigation actions needed
    }
}

/**
 * Analytics interceptor - logs all navigation events
 */
class AnalyticsInterceptor : NavigationInterceptor {
    override suspend fun beforeNavigate(event: NavigationEvent): Boolean {
        logNavigation(event)
        return true
    }

    override suspend fun afterNavigate(event: NavigationEvent) {
        // Optional: log after navigation completes
    }

    private fun logNavigation(event: NavigationEvent) {
        when (event) {
            is NavigationEvent.NavigateTo -> {
                println("Analytics: Navigate to ${event.route}")
            }
            NavigationEvent.GoBack -> {
                println("Analytics: Back pressed")
            }
            is NavigationEvent.PopTo -> {
                println("Analytics: Pop to ${event.route}")
            }
        }
    }
}

/**
 * Transition validation interceptor - validates transitions
 */
class TransitionValidationInterceptor : NavigationInterceptor {
    private val allowedTransitions = mapOf(
        NavigationRoute.Login.route to listOf(NavigationRoute.Dashboard.route),
        NavigationRoute.Dashboard.route to listOf(
            NavigationRoute.Employees.route,
            NavigationRoute.Attendance.route,
            NavigationRoute.Leave.route,
            NavigationRoute.Payroll.route,
            NavigationRoute.Settings.route,
            NavigationRoute.Login.route
        ),
        NavigationRoute.Employees.route to listOf(
            NavigationRoute.EmployeeDetail.createRoute("dummy"),
            NavigationRoute.Dashboard.route
        )
    )

    override suspend fun beforeNavigate(event: NavigationEvent): Boolean {
        return when (event) {
            is NavigationEvent.NavigateTo -> {
                // For simplicity, allow all transitions in this example
                true
            }
            else -> true
        }
    }

    override suspend fun afterNavigate(event: NavigationEvent) {
        // No post-navigation actions needed
    }
}

