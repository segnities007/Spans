package com.segnities007.usecase.auth

import com.segnities007.model.User
import com.segnities007.repository.AuthRepository

class SignInWithGoogleUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(idToken: String): Result<User> {
        // 早期リターン: IDトークンが空白
        if (idToken.isBlank()) {
            return Result.failure(
                IllegalArgumentException("IDトークンが空です")
            )
        }

        return authRepository.signInWithGoogle(idToken)
    }
}
