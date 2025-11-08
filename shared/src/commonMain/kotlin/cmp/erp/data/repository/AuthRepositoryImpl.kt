package cmp.erp.data.repository

import cmp.erp.data.local.TokenStorage
import cmp.erp.data.network.ErpApiClient
import cmp.erp.domain.model.AuthToken
import cmp.erp.domain.model.Result
import cmp.erp.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val apiClient: ErpApiClient,
    private val tokenStorage: TokenStorage
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<AuthToken> {
        return try {
            val response = apiClient.login(email, password)
            if (response.success && response.data != null) {
                tokenStorage.saveToken(response.data.accessToken)
                tokenStorage.saveRefreshToken(response.data.refreshToken.orEmpty())
                Result.Success(response.data)
            } else {
                Result.Error(Exception(response.message ?: "Login failed"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun refreshToken(refreshToken: String): Result<AuthToken> {
        return try {
            val response = apiClient.refreshToken(refreshToken)
            if (response.success && response.data != null) {
                tokenStorage.saveToken(response.data.accessToken)
                tokenStorage.saveRefreshToken(response.data.refreshToken.orEmpty())
                Result.Success(response.data)
            } else {
                Result.Error(Exception(response.message ?: "Token refresh failed"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun logout(): Result<Unit> {
        return try {
            apiClient.logout()
            tokenStorage.clearToken()
            tokenStorage.clearRefreshToken()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override fun getCurrentToken(): String? {
        return tokenStorage.getToken()
    }

    override fun isAuthenticated(): Boolean {
        return tokenStorage.getToken() != null
    }
}

