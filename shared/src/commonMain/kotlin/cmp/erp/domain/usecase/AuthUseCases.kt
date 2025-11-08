package cmp.erp.domain.usecase

import cmp.erp.domain.model.AuthToken
import cmp.erp.domain.model.Result
import cmp.erp.domain.repository.AuthRepository

class LoginUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String): Result<AuthToken> {
        return authRepository.login(email, password)
    }
}

class LogoutUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(): Result<Unit> {
        return authRepository.logout()
    }
}

class RefreshTokenUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(refreshToken: String): Result<AuthToken> {
        return authRepository.refreshToken(refreshToken)
    }
}

class IsAuthenticatedUseCase(private val authRepository: AuthRepository) {
    operator fun invoke(): Boolean {
        return authRepository.isAuthenticated()
    }
}

