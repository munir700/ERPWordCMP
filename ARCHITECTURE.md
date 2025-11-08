# RealWorld ERP Application - Architecture Documentation

## Overview
This is a comprehensive multiplatform ERP (Enterprise Resource Planning) application built with **Clean Architecture** and **MVI (Model-View-Intent)** pattern using Kotlin Multiplatform. The application handles Employee Management, Attendance Tracking, Payroll Management, and Leave Management with secure OAuth authentication and offline support.

## Project Structure

```
shared/src/commonMain/kotlin/cmp/erp/
├── base/                          # Base classes for MVI architecture
│   ├── UiState.kt                # Base interface for UI state
│   ├── UiEvent.kt                # Base interface for UI events/intents
│   ├── UiEffect.kt               # Base interface for side effects
│   ├── MviViewModel.kt           # MVI ViewModel interface
│   └── BaseViewModel.kt          # Abstract base ViewModel implementation
│
├── domain/                        # Domain layer (business logic)
│   ├── model/                    # Domain models
│   │   ├── Employee.kt           # Employee entity
│   │   ├── AttendanceRecord.kt   # Attendance record with status
│   │   ├── Location.kt           # Geo-location data
│   │   ├── Payroll.kt            # Payroll information
│   │   ├── LeaveRequest.kt       # Leave request entity
│   │   ├── ApiModels.kt          # API request/response models
│   │   └── Result.kt             # Generic result wrapper
│   │
│   ├── repository/               # Repository interfaces (contracts)
│   │   ├── EmployeeRepository.kt
│   │   ├── AttendanceRepository.kt
│   │   ├── PayrollRepository.kt
│   │   ├── LeaveRepository.kt
│   │   └── AuthRepository.kt
│   │
│   └── usecase/                  # Use cases/Business logic
│       ├── AuthUseCases.kt       # LoginUseCase, LogoutUseCase, etc.
│       ├── AttendanceUseCases.kt # CheckIn, CheckOut, GetStats, etc.
│       ├── PayrollUseCases.kt    # GetPayroll, ProcessPayroll, etc.
│       ├── LeaveUseCases.kt      # SubmitLeave, ApproveLeave, etc.
│       └── EmployeeUseCases.kt   # GetEmployee, SearchEmployee, SyncEmployee, etc.
│
├── data/                         # Data layer (implementation)
│   ├── network/                  # Network communication
│   │   ├── ErpApiClient.kt       # API client interface
│   │   └── ErpApiClientImpl.kt    # Ktor-based API client implementation
│   │
│   ├── local/                    # Local data storage
│   │   └── LocalDataSource.kt    # Local database interface
│   │
│   └── repository/               # Repository implementations
│       ├── EmployeeRepositoryImpl.kt
│       ├── AttendanceRepositoryImpl.kt
│       ├── PayrollRepositoryImpl.kt
│       ├── LeaveRepositoryImpl.kt
│       └── AuthRepositoryImpl.kt
│
├── presentation/                 # Presentation layer (UI logic)
│   ├── auth/
│   │   └── AuthViewModel.kt      # Authentication ViewModel
│   ├── attendance/
│   │   └── AttendanceViewModel.kt # Attendance check-in/out ViewModel
│   ├── payroll/
│   │   └── PayrollViewModel.kt   # Payroll ViewModel
│   ├── leave/
│   │   └── LeaveViewModel.kt     # Leave management ViewModel
│   └── employee/
│       └── EmployeeViewModel.kt  # Employee management ViewModel
│
└── di/
    └── KoinModule.kt             # Koin DI configuration
```

## Architecture Layers

### 1. **Presentation Layer** (MVI Pattern)
- **Intent/Event**: User actions (CheckIn, CheckOut, SubmitLeave, etc.)
- **View State**: UI state containing data and loading/error states
- **Effect/Side Effect**: One-time events (navigation, toast, dialogs)
- **ViewModel**: Manages state and processes intents

```kotlin
// Example: AttendanceViewModel
data class AttendanceUiState(
    val isCheckedIn: Boolean = false,
    override val isLoading: Boolean = false,
    override val error: String? = null
) : UiState

sealed class AttendanceEvent : UiEvent {
    data class CheckIn(val location: Location) : AttendanceEvent()
    object LoadTodayAttendance : AttendanceEvent()
}

sealed class AttendanceEffect : UiEffect {
    data class CheckInSuccess(val message: String) : AttendanceEffect()
}
```

### 2. **Domain Layer** (Business Logic)
- **Use Cases**: Orchestrate domain logic (CheckInUseCase, SubmitLeaveUseCase)
- **Repositories**: Define data operation contracts
- **Models**: Core domain entities

**Key features:**
- Independent from implementation details
- Contains pure business logic
- Testable without framework dependencies
- No external dependencies

### 3. **Data Layer** (Implementation)

#### Network Communication
```kotlin
ErpApiClientImpl(baseUrl, tokenProvider)
  ├── Ktor HTTP Client with Auth & Content Negotiation
  ├── OAuth Bearer token authentication
  ├── Automatic token refresh
  └── JSON serialization/deserialization
```

#### Local Data Storage
- Platform-specific implementations (Room on Android, SQLite via Room on iOS)
- Offline-first approach: Save locally, sync when connected
- Caching mechanism for fast access

#### Repository Implementations
- Combine network and local data sources
- Implement retry logic and fallback strategies
- Cache management and synchronization

## Key Features

### 1. **Employee Attendance Module**
- **Location-based Check-in/out**: GPS coordinates recorded with each check-in/out
- **Offline Support**: Records stored locally, synced when network available
- **Attendance Statistics**: Calculate working hours, present/absent/half-day counts
- **Real-time Updates**: Push updates to server

```kotlin
// Check-in with location
checkInUseCase(employeeId, Location(lat, lon, accuracy))
```

### 2. **Payroll Management**
- **Monthly Payroll Processing**: Calculate salary with allowances/deductions
- **Batch Processing**: Process payroll for multiple employees
- **Status Tracking**: Draft, Approved, Paid, Pending, Rejected
- **Offline Storage**: Local payroll records cache

### 3. **Leave Management**
- **Multiple Leave Types**: Casual, Sick, Earned, Unpaid, Maternity, Paternity
- **Leave Workflow**: Submit → Approve/Reject → Paid/Cancelled
- **Leave Balance**: Track remaining leaves by type
- **Manager Approvals**: Managers can approve/reject leave requests

### 4. **Employee Management**
- **Directory**: Browse all employees with search
- **Profile Management**: Employee details and contact info
- **Sync from ERP**: Pull latest employee data from backend
- **Local Search**: Quick search with offline employee data

### 5. **Authentication & Security**
- **OAuth 2.0**: Secure authentication with bearer tokens
- **Token Management**: Automatic token refresh on expiry
- **Session Persistence**: Remember user across app restarts
- **Secure Storage**: Encrypted token storage (platform-specific)

## Design Patterns

### 1. **MVI (Model-View-Intent)**
- **Unidirectional Data Flow**: Intent → ViewModel → State → UI
- **Immutable State**: State updates are pure functions
- **One-time Effects**: Side effects handled separately

### 2. **Repository Pattern**
- Abstract data sources behind interfaces
- Combine multiple data sources (network + local)
- Easier testing and maintenance

### 3. **Use Case Pattern**
- Single Responsibility Principle
- Business logic isolated from implementation
- Easy to test and reuse

### 4. **Dependency Injection (Koin)**
- Centralized dependency management
- Easy module swapping for testing
- Lifecycle management

## Data Flow Example: Check-In

```
UI Button Click
    ↓
[AttendanceEvent.CheckIn(location)]
    ↓
AttendanceViewModel.handleEvent()
    ↓
CheckInUseCase()
    ↓
AttendanceRepository.checkIn()
    ↓
┌─────────────────────────────────┐
│ 1. Save locally (immediate)     │
│ 2. Sync with server (async)     │
│ 3. Mark as synced on success    │
│ 4. Retry on failure             │
└─────────────────────────────────┘
    ↓
State Update: isCheckedIn = true
    ↓
UI Re-render
```

## Offline Support Strategy

1. **Local-First Approach**
   - All data written to local database first
   - Marked as "pending sync"
   - UI shows data immediately

2. **Background Sync**
   - Periodic sync of pending records
   - Retry failed records with exponential backoff
   - Notify user of sync status

3. **Conflict Resolution**
   - Server version is authoritative
   - Local changes are discarded if server has updates
   - User notified of conflicts

## Network Stack (Ktor Client)

```kotlin
HttpClient {
    install(ContentNegotiation) {
        json(Json { /* serialization config */ })
    }
    
    install(Auth) {
        bearer {
            loadTokens { BearerTokens(tokenProvider(), "") }
            refreshTokens { /* auto-refresh on 401 */ }
        }
    }
}
```

## Dependency Injection Setup

```kotlin
setupKoin {
    modules(
        networkModule,      // API client
        dataSourceModule,   // Local DB
        repositoryModule,   // Repository impls
        useCaseModule,      // Use cases
        viewModelModule     // ViewModels
    )
}
```

## Use Case Implementations

### Authentication Use Cases
```kotlin
LoginUseCase(email, password) → AuthToken
RefreshTokenUseCase(refreshToken) → AuthToken
LogoutUseCase() → Unit
IsAuthenticatedUseCase() → Boolean
```

### Attendance Use Cases
```kotlin
CheckInUseCase(employeeId, location) → AttendanceRecord
CheckOutUseCase(employeeId, location) → AttendanceRecord
GetTodayAttendanceUseCase(employeeId) → Flow<AttendanceRecord?>
GetAttendanceRecordsUseCase(employeeId, startDate, endDate) → Flow<List<AttendanceRecord>>
GetAttendanceStatsUseCase(employeeId, month, year) → Flow<AttendanceStats>
SyncOfflineAttendanceUseCase() → Result<Unit>
```

### Payroll Use Cases
```kotlin
GetEmployeePayrollUseCase(employeeId) → Flow<List<Payroll>>
GetPayrollByMonthYearUseCase(employeeId, month, year) → Flow<Payroll?>
SyncPayrollUseCase() → Result<Unit>
ProcessPayrollUseCase(employeeIds, month, year) → Result<List<Payroll>>
```

### Leave Use Cases
```kotlin
GetEmployeeLeaveUseCase(employeeId) → Flow<List<LeaveRequest>>
GetPendingLeavesUseCase(employeeId) → Flow<List<LeaveRequest>>
SubmitLeaveRequestUseCase(leaveRequest) → Result<LeaveRequest>
ApproveLeaveUseCase(leaveId, approvedBy) → Result<LeaveRequest>
RejectLeaveUseCase(leaveId, rejectionReason) → Result<LeaveRequest>
GetLeaveBalanceUseCase(employeeId) → Flow<LeaveBalance>
```

### Employee Use Cases
```kotlin
GetAllEmployeesUseCase() → Flow<List<Employee>>
GetEmployeeByIdUseCase(employeeId) → Flow<Employee>
SearchEmployeesUseCase(query) → Flow<List<Employee>>
SyncEmployeesUseCase() → Result<Unit>
```

## Error Handling

All operations return `Result<T>` sealed class:

```kotlin
sealed class Result<T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<T>()
    object Loading : Result<T>()
}
```

## Testing Strategy

1. **Unit Tests**: Domain layer (use cases, repositories)
2. **Integration Tests**: Data layer with mock API
3. **ViewModel Tests**: State transitions and effects
4. **UI Tests**: Compose previews and interactions

## Platform-Specific Implementations

### Android
- Room Database for local storage
- Android EncryptedSharedPreferences for token storage
- Android Location Services for GPS

### iOS
- SQLite via Room (KMP support)
- Keychain for secure token storage
- CoreLocation for GPS

### Web
- IndexedDB for local storage
- LocalStorage for tokens (for demo, use secure alternatives in production)
- Geolocation API for GPS

## API Endpoints

### Authentication
- `POST /auth/login` - User login
- `POST /auth/refresh` - Refresh token
- `POST /auth/logout` - Logout

### Attendance
- `POST /attendance/{employeeId}/check-in` - Check-in
- `POST /attendance/{employeeId}/check-out` - Check-out
- `GET /attendance/{employeeId}?startDate=...&endDate=...` - Get records
- `POST /attendance/sync` - Sync offline records

### Employee
- `GET /employees` - Get all employees
- `GET /employees/{id}` - Get employee
- `GET /employees?search=...` - Search employees

### Payroll
- `GET /payroll/{employeeId}` - Get payroll records
- `GET /payroll/{employeeId}/{year}/{month}` - Get specific payroll
- `POST /payroll/process` - Process payroll batch

### Leave
- `GET /leaves/{employeeId}` - Get leaves
- `POST /leaves` - Submit leave request
- `POST /leaves/{id}/approve` - Approve leave
- `POST /leaves/{id}/reject` - Reject leave

## Security Considerations

1. **Token Security**
   - Tokens stored in secure platform-specific storage
   - Automatic refresh before expiry
   - Clear on logout

2. **API Security**
   - OAuth 2.0 Bearer token authentication
   - HTTPS only (enforce in production)
   - Request validation and sanitization

3. **Local Storage**
   - Encrypt sensitive data at rest
   - Use platform-specific secure storage
   - Clear sensitive data on logout

## Future Enhancements

1. **Advanced Features**
   - Biometric authentication
   - Voice/video call integration
   - Real-time notifications
   - Advanced analytics dashboard

2. **Performance**
   - GraphQL API support
   - WebSocket for real-time updates
   - Image compression and caching

3. **Scalability**
   - Pagination for large datasets
   - Database query optimization
   - API rate limiting

## Dependencies

### Core
- Kotlin Coroutines
- Kotlin Flow
- Kotlinx Serialization
- Kotlinx DateTime

### Network
- Ktor Client
- Ktor Auth
- Ktor Content Negotiation

### Local Storage
- Room Database
- Encrypted Preferences

### DI
- Koin Core

### Testing
- Kotlin Test
- JUnit

## Getting Started

### Setup DI
```kotlin
setupKoin {
    // Initialize Koin
}
```

### Initialize ViewModel
```kotlin
val authViewModel: AuthViewModel = get()
val attendanceViewModel: AttendanceViewModel = get { parametersOf(employeeId) }
```

### Observe State and Effects
```kotlin
authViewModel.uiState.collect { state ->
    // Update UI with state
}

authViewModel.uiEffect.collect { effect ->
    // Handle one-time effects (navigation, toasts)
}
```

## Summary

This architecture provides:
- ✅ Clean separation of concerns
- ✅ Testable code at all layers
- ✅ Reusable components across platforms
- ✅ Offline-first approach
- ✅ Secure authentication
- ✅ Type-safe implementations
- ✅ Reactive data flow with Kotlin Flow
- ✅ Easy to extend and maintain

