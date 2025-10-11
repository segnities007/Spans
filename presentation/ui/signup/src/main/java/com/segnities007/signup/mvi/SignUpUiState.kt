package com.segnities007.signup.mvi

import com.segnities007.model.User
import com.segnities007.mvi.UiState

sealed interface SignUpUiState : UiState {
    data class Wait(
        val nickname: String = "",
        val bio: String = "",
        val avatarUri: String? = null,
        val nicknameError: String? = null,
        val bioError: String? = null,
        val isSubmitting: Boolean = false
    ) : SignUpUiState {
        val isValid: Boolean
            get() = nicknameError == null && bioError == null

        val canSubmit: Boolean
            get() = !isSubmitting && isValid && nickname.isNotBlank()
    }

    data class Success(val user: User) : SignUpUiState

    data class Failed(
        val nickname: String,
        val bio: String,
        val avatarUri: String?,
        val nicknameError: String?,
        val bioError: String?
    ) : SignUpUiState
}