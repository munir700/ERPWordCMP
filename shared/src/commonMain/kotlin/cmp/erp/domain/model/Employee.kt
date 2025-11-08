package cmp.erp.domain.model

import kotlinx.serialization.Serializable
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalDate

/**
 * Employee entity representing an employee in the ERP system
 */
@Serializable
data class Employee(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val department: String,
    val position: String,
    val employeeCode: String,
    val dateOfJoining: LocalDate,
    val isActive: Boolean = true,
    val profileImageUrl: String? = null,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null
)

