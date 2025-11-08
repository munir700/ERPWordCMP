package cmp.erp.domain.repository

import cmp.erp.domain.model.AuthToken
import cmp.erp.domain.model.Result

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<AuthToken>
    suspend fun refreshToken(refreshToken: String): Result<AuthToken>
    suspend fun logout(): Result<Unit>
    fun getCurrentToken(): String?
    fun isAuthenticated(): Boolean
}

