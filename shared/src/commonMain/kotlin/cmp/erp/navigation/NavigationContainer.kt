package cmp.erp.navigation

/**
 * Navigation routes definition for building screen routing logic
 * This is a common module file - actual composition happens in Android module
 */

/**
 * Navigation event sealed class for route changes
 */
sealed class NavigationScreenEvent {
    data class OnLoginScreen(val onNavigateToDashboard: () -> Unit) : NavigationScreenEvent()
    data class OnDashboardScreen(
        val onNavigateToEmployees: () -> Unit,
        val onNavigateToAttendance: () -> Unit,
        val onNavigateToLeave: () -> Unit,
        val onNavigateToPayroll: () -> Unit,
        val onNavigateToSettings: () -> Unit,
        val onLogout: () -> Unit
    ) : NavigationScreenEvent()
    data class OnEmployeesScreen(
        val onNavigateBack: () -> Unit,
        val onNavigateToDetail: (String) -> Unit
    ) : NavigationScreenEvent()
    data class OnEmployeeDetailScreen(
        val employeeId: String,
        val onNavigateBack: () -> Unit
    ) : NavigationScreenEvent()
    data class OnAttendanceScreen(val onNavigateBack: () -> Unit) : NavigationScreenEvent()
    data class OnLeaveScreen(val onNavigateBack: () -> Unit) : NavigationScreenEvent()
    data class OnPayrollScreen(val onNavigateBack: () -> Unit) : NavigationScreenEvent()
    data class OnSettingsScreen(val onNavigateBack: () -> Unit) : NavigationScreenEvent()
}

/**
 * Helper to determine which screen should be shown based on current route
 */
fun getScreenForRoute(route: String): String = route.substringBefore("?").substringBefore("{")

/**
 * Check if route matches pattern with arguments
 */
fun routeMatches(route: String, pattern: String): Boolean {
    return route.startsWith(pattern.substringBefore("{"))
}

/**
 * Extract arguments from route
 */
fun extractArguments(route: String): Map<String, String> {
    val argsMap = mutableMapOf<String, String>()
    if (route.contains("/")) {
        val parts = route.split("/")
        if (parts.size > 1) {
            argsMap["id"] = parts.last()
        }
    }
    return argsMap
}
