package cmp.erp.navigation

/**
 * Sealed class defining all navigation routes in the app
 */
sealed class NavigationRoute(val route: String) {
    object Login : NavigationRoute("login")
    object Dashboard : NavigationRoute("dashboard")
    object Employees : NavigationRoute("employees")
    object EmployeeDetail : NavigationRoute("employees/{employeeId}") {
        fun createRoute(employeeId: String) = "employees/$employeeId"
    }
    object Attendance : NavigationRoute("attendance")
    object Leave : NavigationRoute("leave")
    object Payroll : NavigationRoute("payroll")
    object Settings : NavigationRoute("settings")
}

