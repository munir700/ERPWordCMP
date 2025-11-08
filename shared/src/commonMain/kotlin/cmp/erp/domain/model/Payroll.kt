package cmp.erp.domain.model

import kotlinx.serialization.Serializable
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalDate

/**
 * Payroll entity for salary and payment information
 */
@Serializable
data class Payroll(
    val id: String,
    val employeeId: String,
    val month: Int,
    val year: Int,
    val baseSalary: Double,
    val allowances: Double = 0.0,
    val deductions: Double = 0.0,
    val bonuses: Double = 0.0,
    val netSalary: Double,
    val status: PayrollStatus = PayrollStatus.DRAFT,
    val paymentDate: LocalDate? = null,
    val remarks: String? = null,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null
)

@Serializable
enum class PayrollStatus {
    DRAFT, APPROVED, PAID, PENDING, REJECTED
}

