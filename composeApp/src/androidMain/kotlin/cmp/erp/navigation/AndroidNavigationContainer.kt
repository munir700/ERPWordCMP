package cmp.erp.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import cmp.erp.presentation.screens.AttendanceScreen
import cmp.erp.presentation.screens.DashboardScreen
import cmp.erp.presentation.screens.EmployeeDetailScreen
import cmp.erp.presentation.screens.EmployeesScreen
import cmp.erp.presentation.screens.LeaveScreen
import cmp.erp.presentation.screens.LoginScreen
import cmp.erp.presentation.screens.PayrollScreen
import cmp.erp.presentation.screens.SettingsScreen

/**
 * Navigation container that renders the appropriate screen based on current route
 */
@Composable
fun NavigationContainer(navigationStateHolder: NavigationStateHolder) {
    val currentRoute = navigationStateHolder.currentRoute.collectAsState()

    when (currentRoute.value) {
        NavigationRoute.Login.route -> {
            LoginScreen(
                onNavigateToDashboard = {
                    navigationStateHolder.clearAndNavigateTo(NavigationRoute.Dashboard.route)
                }
            )
        }

        NavigationRoute.Dashboard.route -> {
            DashboardScreen(
                onNavigateToEmployees = {
                    navigationStateHolder.navigateTo(NavigationRoute.Employees.route)
                },
                onNavigateToAttendance = {
                    navigationStateHolder.navigateTo(NavigationRoute.Attendance.route)
                },
                onNavigateToLeave = {
                    navigationStateHolder.navigateTo(NavigationRoute.Leave.route)
                },
                onNavigateToPayroll = {
                    navigationStateHolder.navigateTo(NavigationRoute.Payroll.route)
                },
                onNavigateToSettings = {
                    navigationStateHolder.navigateTo(NavigationRoute.Settings.route)
                },
                onLogout = {
                    navigationStateHolder.resetToLogin()
                }
            )
        }

        NavigationRoute.Employees.route -> {
            EmployeesScreen(
                onNavigateBack = {
                    navigationStateHolder.goBack()
                },
                onNavigateToDetail = { empId ->
                    navigationStateHolder.navigateTo(
                        NavigationRoute.EmployeeDetail.createRoute(empId)
                    )
                }
            )
        }

        NavigationRoute.Attendance.route -> {
            AttendanceScreen(
                onNavigateBack = {
                    navigationStateHolder.goBack()
                },
                onCheckIn = { /* Handle check-in */ },
                onCheckOut = { /* Handle check-out */ }
            )
        }

        NavigationRoute.Leave.route -> {
            LeaveScreen(
                onNavigateBack = {
                    navigationStateHolder.goBack()
                },
                onSubmitLeave = { leaveType, fromDate, toDate ->
                    // Handle leave submission
                },
                onViewBalance = { /* Handle view balance */ }
            )
        }

        NavigationRoute.Payroll.route -> {
            PayrollScreen(
                onNavigateBack = {
                    navigationStateHolder.goBack()
                },
                onViewPayslip = { slipId ->
                    // Handle view payslip
                },
                onProcessPayroll = { /* Handle process payroll */ }
            )
        }

        NavigationRoute.Settings.route -> {
            SettingsScreen(
                onNavigateBack = {
                    navigationStateHolder.goBack()
                },
                onLogout = {
                    navigationStateHolder.resetToLogin()
                }
            )
        }

        else -> {
            // Handle employee detail routes
            if (currentRoute.value.startsWith("employees/")) {
                val empId = currentRoute.value.substringAfter("employees/")
                EmployeeDetailScreen(
                    employeeId = empId,
                    onNavigateBack = {
                        navigationStateHolder.goBack()
                    }
                )
            } else {
                // Fallback to dashboard
                DashboardScreen(
                    onNavigateToEmployees = {
                        navigationStateHolder.navigateTo(NavigationRoute.Employees.route)
                    },
                    onNavigateToAttendance = {
                        navigationStateHolder.navigateTo(NavigationRoute.Attendance.route)
                    },
                    onNavigateToLeave = {
                        navigationStateHolder.navigateTo(NavigationRoute.Leave.route)
                    },
                    onNavigateToPayroll = {
                        navigationStateHolder.navigateTo(NavigationRoute.Payroll.route)
                    },
                    onNavigateToSettings = {
                        navigationStateHolder.navigateTo(NavigationRoute.Settings.route)
                    },
                    onLogout = {
                        navigationStateHolder.resetToLogin()
                    }
                )
            }
        }
    }
}

/**
 * Get animated transitions for screen changes
 */
fun getEnterTransition() = slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn()

fun getExitTransition() = slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut()

fun getPopEnterTransition() = slideInHorizontally(initialOffsetX = { -1000 }) + fadeIn()

fun getPopExitTransition() = slideOutHorizontally(targetOffsetX = { 1000 }) + fadeOut()
