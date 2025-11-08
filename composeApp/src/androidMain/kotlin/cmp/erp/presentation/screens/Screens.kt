package cmp.erp.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// ==================== LOGIN SCREEN ====================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(onNavigateToDashboard: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ERP Login") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Welcome to ERP System", style = MaterialTheme.typography.headlineLarge, modifier = Modifier.padding(bottom = 32.dp))
            Text("Login with your credentials", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(bottom = 32.dp))
            Button(onClick = onNavigateToDashboard, modifier = Modifier.fillMaxWidth(0.7f)) {
                Text("Login")
            }
        }
    }
}

// ==================== DASHBOARD SCREEN ====================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onNavigateToEmployees: () -> Unit,
    onNavigateToAttendance: () -> Unit,
    onNavigateToLeave: () -> Unit,
    onNavigateToPayroll: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onLogout: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dashboard") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { Button(onClick = onNavigateToEmployees, modifier = Modifier.fillMaxWidth()) { Text("👥 Employees") } }
            item { Button(onClick = onNavigateToAttendance, modifier = Modifier.fillMaxWidth()) { Text("📋 Attendance") } }
            item { Button(onClick = onNavigateToLeave, modifier = Modifier.fillMaxWidth()) { Text("📅 Leave Requests") } }
            item { Button(onClick = onNavigateToPayroll, modifier = Modifier.fillMaxWidth()) { Text("💰 Payroll") } }
            item { Button(onClick = onNavigateToSettings, modifier = Modifier.fillMaxWidth()) { Text("⚙️ Settings") } }
            item { Button(onClick = onLogout, modifier = Modifier.fillMaxWidth()) { Text("🚪 Logout") } }
        }
    }
}

// ==================== EMPLOYEES SCREEN ====================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeesScreen(
    onNavigateBack: () -> Unit,
    onNavigateToDetail: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Employees") },
                navigationIcon = { Button(onClick = onNavigateBack) { Text("← Back") } },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item { Text("Employee List", style = MaterialTheme.typography.headlineSmall) }
            items(3) { index ->
                EmployeeCard("emp-${index + 1}", "Employee ${index + 1}", "Department ${(index % 3) + 1}") { onNavigateToDetail("emp-${index + 1}") }
            }
        }
    }
}

@Composable
private fun EmployeeCard(id: String, name: String, dept: String, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Text(name, style = MaterialTheme.typography.titleMedium)
            Text(dept, style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = onClick, modifier = Modifier.fillMaxWidth()) { Text("View Details") }
        }
    }
}

// ==================== EMPLOYEE DETAIL SCREEN ====================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeDetailScreen(employeeId: String, onNavigateBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Employee: $employeeId") },
                navigationIcon = { Button(onClick = onNavigateBack) { Text("← Back") } },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Text("Employee Details", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(16.dp))
            DetailRow("ID", employeeId)
            DetailRow("Name", "John Doe")
            DetailRow("Department", "Engineering")
            DetailRow("Position", "Senior Developer")
            DetailRow("Status", "Active")
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Text(label, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
        Text(value, style = MaterialTheme.typography.bodySmall, modifier = Modifier.weight(1f))
    }
}

// ==================== ATTENDANCE SCREEN ====================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceScreen(
    onNavigateBack: () -> Unit,
    onCheckIn: () -> Unit = {},
    onCheckOut: () -> Unit = {}
) {
    val isCheckedIn = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Attendance") },
                navigationIcon = { Button(onClick = onNavigateBack) { Text("← Back") } },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Text("Today's Attendance", style = MaterialTheme.typography.headlineSmall) }
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Status: ${if (isCheckedIn.value) "Checked In ✓" else "Not Checked In"}", style = MaterialTheme.typography.bodyLarge)
                        Spacer(modifier = Modifier.height(12.dp))
                        if (!isCheckedIn.value) {
                            Button(onClick = { isCheckedIn.value = true; onCheckIn() }, modifier = Modifier.fillMaxWidth()) { Text("Check In") }
                        } else {
                            Button(onClick = { isCheckedIn.value = false; onCheckOut() }, modifier = Modifier.fillMaxWidth()) { Text("Check Out") }
                        }
                    }
                }
            }
            item { Text("Attendance History", style = MaterialTheme.typography.headlineSmall) }
            items(5) { index ->
                AttendanceRecordCard("2025-11-${8 - index}", "09:00 AM", "06:00 PM", "Present")
            }
        }
    }
}

@Composable
private fun AttendanceRecordCard(date: String, checkIn: String, checkOut: String, status: String) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(date, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
                Text(status, style = MaterialTheme.typography.bodySmall)
            }
            Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
                Text("In: $checkIn", style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.weight(1f))
                Text("Out: $checkOut", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

// ==================== LEAVE SCREEN ====================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaveScreen(
    onNavigateBack: () -> Unit,
    onSubmitLeave: (String, String, String) -> Unit = { _, _, _ -> },
    onViewBalance: () -> Unit = {}
) {
    val leaveType = remember { mutableStateOf("Casual") }
    val fromDate = remember { mutableStateOf("") }
    val toDate = remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Leave Management") },
                navigationIcon = { Button(onClick = onNavigateBack) { Text("← Back") } },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { Text("Leave Balance", style = MaterialTheme.typography.headlineSmall); Button(onClick = onViewBalance, modifier = Modifier.fillMaxWidth()) { Text("View Leave Balance") } }
            item { Text("Apply for Leave", style = MaterialTheme.typography.headlineSmall) }
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        LeaveTypeSelector(leaveType.value) { leaveType.value = it }
                        Spacer(modifier = Modifier.height(12.dp))
                        DateInputField("From Date") { fromDate.value = it }
                        Spacer(modifier = Modifier.height(8.dp))
                        DateInputField("To Date") { toDate.value = it }
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(onClick = { onSubmitLeave(leaveType.value, fromDate.value, toDate.value) }, modifier = Modifier.fillMaxWidth()) { Text("Submit Request") }
                    }
                }
            }
            item { Text("Leave Requests", style = MaterialTheme.typography.headlineSmall) }
            items(3) { index ->
                LeaveRequestCard(listOf("Casual", "Sick", "Earned")[index], "2025-11-${10 + index}", "2025-11-${12 + index}", listOf("Pending", "Approved", "Rejected")[index])
            }
        }
    }
}

@Composable
private fun LeaveTypeSelector(selected: String, onSelect: (String) -> Unit) {
    val types = listOf("Casual", "Sick", "Earned", "Unpaid")
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        types.forEach { type ->
            Button(onClick = { onSelect(type) }, modifier = Modifier.weight(1f)) { Text(type) }
        }
    }
}

@Composable
private fun DateInputField(label: String, onValueChange: (String) -> Unit) {
    Text(label, style = MaterialTheme.typography.labelSmall)
    Button(onClick = { onValueChange("2025-11-08") }, modifier = Modifier.fillMaxWidth()) { Text("Select Date") }
}

@Composable
private fun LeaveRequestCard(leaveType: String, startDate: String, endDate: String, status: String) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(leaveType, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
                Text(status, style = MaterialTheme.typography.bodySmall)
            }
            Text("$startDate to $endDate", style = MaterialTheme.typography.bodySmall)
        }
    }
}

// ==================== PAYROLL SCREEN ====================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PayrollScreen(
    onNavigateBack: () -> Unit,
    onViewPayslip: (String) -> Unit = {},
    onProcessPayroll: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Payroll Management") },
                navigationIcon = { Button(onClick = onNavigateBack) { Text("← Back") } },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { Button(onClick = onProcessPayroll, modifier = Modifier.fillMaxWidth()) { Text("Process Payroll") } }
            item { Text("Payslips", style = MaterialTheme.typography.headlineSmall) }
            items(6) { index ->
                PayslipCard(listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun")[index], "$${5000 + (index * 100)}", "Processed") { onViewPayslip("payslip-${index + 1}") }
            }
        }
    }
}

@Composable
private fun PayslipCard(month: String, amount: String, status: String, onView: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(month, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
                Text(amount, style = MaterialTheme.typography.bodyMedium)
            }
            Text(status, style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = onView, modifier = Modifier.fillMaxWidth()) { Text("View Payslip") }
        }
    }
}

// ==================== SETTINGS SCREEN ====================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    onLogout: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = { Button(onClick = onNavigateBack) { Text("← Back") } },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Application Settings", style = MaterialTheme.typography.headlineSmall)
            Button(onClick = { }, modifier = Modifier.fillMaxWidth()) { Text("Profile Settings") }
            Button(onClick = { }, modifier = Modifier.fillMaxWidth()) { Text("Notification Settings") }
            Button(onClick = { }, modifier = Modifier.fillMaxWidth()) { Text("About") }
            Spacer(modifier = Modifier.weight(1f))
            Button(onClick = onLogout, modifier = Modifier.fillMaxWidth()) { Text("Logout") }
        }
    }
}

