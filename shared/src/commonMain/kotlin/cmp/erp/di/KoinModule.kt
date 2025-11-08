package cmp.erp.di

import cmp.erp.data.local.InMemoryTokenStorage
import cmp.erp.data.local.LocalDataSource
import cmp.erp.data.local.PlaceholderLocalDataSource
import cmp.erp.data.local.TokenStorage
import cmp.erp.data.network.ErpApiClient
import cmp.erp.data.network.ErpApiClientImpl
import cmp.erp.data.repository.AttendanceRepositoryImpl
import cmp.erp.data.repository.AuthRepositoryImpl
import cmp.erp.data.repository.EmployeeRepositoryImpl
import cmp.erp.data.repository.LeaveRepositoryImpl
import cmp.erp.data.repository.PayrollRepositoryImpl
import cmp.erp.domain.repository.AttendanceRepository
import cmp.erp.domain.repository.AuthRepository
import cmp.erp.domain.repository.EmployeeRepository
import cmp.erp.domain.repository.LeaveRepository
import cmp.erp.domain.repository.PayrollRepository
import cmp.erp.domain.usecase.ApproveLeaveUseCase
import cmp.erp.domain.usecase.CheckInUseCase
import cmp.erp.domain.usecase.CheckOutUseCase
import cmp.erp.domain.usecase.GetAllEmployeesUseCase
import cmp.erp.domain.usecase.GetAttendanceRecordsUseCase
import cmp.erp.domain.usecase.GetAttendanceStatsUseCase
import cmp.erp.domain.usecase.GetEmployeeByIdUseCase
import cmp.erp.domain.usecase.GetEmployeeLeaveUseCase
import cmp.erp.domain.usecase.GetEmployeePayrollUseCase
import cmp.erp.domain.usecase.GetLeaveBalanceUseCase
import cmp.erp.domain.usecase.GetPayrollByMonthYearUseCase
import cmp.erp.domain.usecase.GetPendingLeavesUseCase
import cmp.erp.domain.usecase.GetTodayAttendanceUseCase
import cmp.erp.domain.usecase.IsAuthenticatedUseCase
import cmp.erp.domain.usecase.LoginUseCase
import cmp.erp.domain.usecase.LogoutUseCase
import cmp.erp.domain.usecase.ProcessPayrollUseCase
import cmp.erp.domain.usecase.RejectLeaveUseCase
import cmp.erp.domain.usecase.RefreshTokenUseCase
import cmp.erp.domain.usecase.SearchEmployeesUseCase
import cmp.erp.domain.usecase.SubmitLeaveRequestUseCase
import cmp.erp.domain.usecase.SyncEmployeesUseCase
import cmp.erp.domain.usecase.SyncOfflineAttendanceUseCase
import cmp.erp.domain.usecase.SyncPayrollUseCase
import cmp.erp.presentation.attendance.AttendanceViewModel
import cmp.erp.presentation.auth.AuthViewModel
import cmp.erp.presentation.employee.EmployeeViewModel
import cmp.erp.presentation.leave.LeaveViewModel
import cmp.erp.presentation.payroll.PayrollViewModel
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

/**
 * Setup Koin dependency injection for the ERP application
 */
fun setupKoin(appDeclaration: KoinAppDeclaration = {}) {
    startKoin {
        appDeclaration()
        modules(
            networkModule,
            dataSourceModule,
            repositoryModule,
            useCaseModule,
            viewModelModule
        )
    }
}

/**
 * Network layer module
 */
private val networkModule = module {
    single<ErpApiClient> {
        ErpApiClientImpl(
            baseUrl = "https://api.erp.local",
            tokenProvider = { get<TokenStorage>().getToken() }
        )
    }
}

/**
 * Data source module (for local database)
 * Note: Implementation depends on platform-specific Room setup
 */
private val dataSourceModule = module {
    single<LocalDataSource> {
        PlaceholderLocalDataSource()
    }

    single<TokenStorage> {
        InMemoryTokenStorage()
    }
}

/**
 * Repository module
 */
private val repositoryModule = module {
    single<EmployeeRepository> { EmployeeRepositoryImpl(get(), get()) }
    single<AttendanceRepository> { AttendanceRepositoryImpl(get(), get()) }
    single<PayrollRepository> { PayrollRepositoryImpl(get(), get()) }
    single<LeaveRepository> { LeaveRepositoryImpl(get(), get()) }
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
}

/**
 * Use cases module
 */
private val useCaseModule = module {
    single { LoginUseCase(get()) }
    single { LogoutUseCase(get()) }
    single { RefreshTokenUseCase(get()) }
    single { IsAuthenticatedUseCase(get()) }

    single { CheckInUseCase(get()) }
    single { CheckOutUseCase(get()) }
    single { GetTodayAttendanceUseCase(get()) }
    single { GetAttendanceRecordsUseCase(get()) }
    single { GetAttendanceStatsUseCase(get()) }
    single { SyncOfflineAttendanceUseCase(get()) }

    single { GetEmployeePayrollUseCase(get()) }
    single { GetPayrollByMonthYearUseCase(get()) }
    single { SyncPayrollUseCase(get()) }
    single { ProcessPayrollUseCase(get()) }

    single { GetEmployeeLeaveUseCase(get()) }
    single { GetPendingLeavesUseCase(get()) }
    single { SubmitLeaveRequestUseCase(get()) }
    single { ApproveLeaveUseCase(get()) }
    single { RejectLeaveUseCase(get()) }
    single { GetLeaveBalanceUseCase(get()) }

    single { GetAllEmployeesUseCase(get()) }
    single { GetEmployeeByIdUseCase(get()) }
    single { SearchEmployeesUseCase(get()) }
    single { SyncEmployeesUseCase(get()) }
}

/**
 * ViewModels module
 */
private val viewModelModule = module {
    single { AuthViewModel(get(), get(), get()) }
    factory { (employeeId: String) ->
        AttendanceViewModel(get(), get(), get(), employeeId)
    }
    factory { (employeeId: String) ->
        LeaveViewModel(get(), get(), get(), employeeId)
    }
    factory { (employeeId: String) ->
        PayrollViewModel(get(), get(), employeeId)
    }
    single { EmployeeViewModel(get(), get(), get(), get()) }
}


