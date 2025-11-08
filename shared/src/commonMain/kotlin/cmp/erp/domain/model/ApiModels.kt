package cmp.erp.domain.model

import kotlinx.serialization.Serializable

/**
 * API Response wrapper for handling API responses
 */
@Serializable
data class ApiResponse<T>(
    val success: Boolean,
    val message: String? = null,
    val data: T? = null,
    val errors: List<String>? = null
)

/**
 * Authentication token data
 */
@Serializable
data class AuthToken(
    val accessToken: String,
    val refreshToken: String? = null,
    val expiresIn: Long? = null,
    val tokenType: String = "Bearer"
)

/**
 * User login credentials
 */
@Serializable
data class LoginCredentials(
    val email: String,
    val password: String
)

