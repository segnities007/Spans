package com.segnities007.signup.mvi

import com.segnities007.model.User
import com.segnities007.mvi.Reducer

class SignUpReducer : Reducer<SignUpUiState, SignUpIntent> {

    override fun reduce(state: SignUpUiState, intent: SignUpIntent): SignUpUiState {
        return when (intent) {
            is SignUpIntent.NicknameChanged -> reduceNicknameChanged(state, intent)
            is SignUpIntent.BioChanged -> reduceBioChanged(state, intent)
            is SignUpIntent.AvatarSelected -> reduceAvatarSelected(state, intent)
            is SignUpIntent.AvatarRemoved -> reduceAvatarRemoved(state, intent)
            is SignUpIntent.SignUpClicked -> reduceSignUpStarted(state, intent)
            is SignUpIntent.NavigateToSignIn -> state // 状態変更なし
            is SignUpIntent.RetryClicked -> reduceRetry(state, intent)
        }
    }

    private fun reduceNicknameChanged(
        state: SignUpUiState,
        intent: SignUpIntent.NicknameChanged
    ): SignUpUiState {
        // 早期リターン: 編集状態以外は無視
        if (state !is SignUpUiState.Editing && state !is SignUpUiState.Error) {
            return state
        }

        val trimmedNickname = intent.nickname.trim()
        val nicknameError = validateNickname(trimmedNickname)

        val bio = when (state) {
            is SignUpUiState.Editing -> state.bio
            is SignUpUiState.Error -> state.bio
            else -> ""
        }

        val avatarUri = when (state) {
            is SignUpUiState.Editing -> state.avatarUri
            is SignUpUiState.Error -> state.avatarUri
            else -> null
        }

        val bioError = validateBio(bio)

        return SignUpUiState.Editing(
            nickname = trimmedNickname,
            bio = bio,
            avatarUri = avatarUri,
            nicknameError = nicknameError,
            bioError = bioError,
            isFormValid = nicknameError == null && bioError == null && trimmedNickname.isNotBlank()
        )
    }

    private fun reduceBioChanged(
        state: SignUpUiState,
        intent: SignUpIntent.BioChanged
    ): SignUpUiState {
        // 早期リターン: 編集状態以外は無視
        if (state !is SignUpUiState.Editing && state !is SignUpUiState.Error) {
            return state
        }

        val trimmedBio = intent.bio.trim()
        val bioError = validateBio(trimmedBio)

        val nickname = when (state) {
            is SignUpUiState.Editing -> state.nickname
            is SignUpUiState.Error -> state.nickname
            else -> ""
        }

        val avatarUri = when (state) {
            is SignUpUiState.Editing -> state.avatarUri
            is SignUpUiState.Error -> state.avatarUri
            else -> null
        }

        val nicknameError = validateNickname(nickname)

        return SignUpUiState.Editing(
            nickname = nickname,
            bio = trimmedBio,
            avatarUri = avatarUri,
            nicknameError = nicknameError,
            bioError = bioError,
            isFormValid = nicknameError == null && bioError == null && nickname.isNotBlank()
        )
    }

    private fun reduceAvatarSelected(
        state: SignUpUiState,
        intent: SignUpIntent.AvatarSelected
    ): SignUpUiState {
        // 早期リターン: 編集状態以外は無視
        if (state !is SignUpUiState.Editing && state !is SignUpUiState.Error) {
            return state
        }

        return when (state) {
            is SignUpUiState.Editing -> state.copy(avatarUri = intent.uri)
            is SignUpUiState.Error -> SignUpUiState.Editing(
                nickname = state.nickname,
                bio = state.bio,
                avatarUri = intent.uri,
                nicknameError = validateNickname(state.nickname),
                bioError = validateBio(state.bio),
                isFormValid = validateNickname(state.nickname) == null &&
                        validateBio(state.bio) == null &&
                        state.nickname.isNotBlank()
            )
            else -> state
        }
    }

    private fun reduceAvatarRemoved(
        state: SignUpUiState,
        intent: SignUpIntent.AvatarRemoved
    ): SignUpUiState {
        // AvatarSelectedと同じロジック（uri = null）
        return when (state) {
            is SignUpUiState.Editing -> state.copy(avatarUri = null)
            is SignUpUiState.Error -> SignUpUiState.Editing(
                nickname = state.nickname,
                bio = state.bio,
                avatarUri = null,
                nicknameError = validateNickname(state.nickname),
                bioError = validateBio(state.bio),
                isFormValid = validateNickname(state.nickname) == null &&
                        validateBio(state.bio) == null &&
                        state.nickname.isNotBlank()
            )
            else -> state
        }
    }

    private fun reduceSignUpStarted(
        state: SignUpUiState,
        intent: SignUpIntent.SignUpClicked
    ): SignUpUiState {
        // 編集状態以外は無視
        if (state !is SignUpUiState.Editing) return state
        
        // フォームが無効なら無視
        if (!state.isFormValid) return state

        return SignUpUiState.Loading(
            nickname = state.nickname,
            bio = state.bio,
            avatarUri = state.avatarUri
        )
    }

    private fun reduceRetry(
        state: SignUpUiState,
        intent: SignUpIntent.RetryClicked
    ): SignUpUiState {
        // エラー状態以外は無視
        if (state !is SignUpUiState.Error) return state

        return SignUpUiState.Editing(
            nickname = state.nickname,
            bio = state.bio,
            avatarUri = state.avatarUri,
            nicknameError = validateNickname(state.nickname),
            bioError = validateBio(state.bio),
            isFormValid = validateNickname(state.nickname) == null &&
                    validateBio(state.bio) == null &&
                    state.nickname.isNotBlank()
        )
    }

    private fun validateNickname(nickname: String): String? {
        return when {
            nickname.isBlank() -> "ニックネームを入力してください"
            nickname.length < User.MIN_NICKNAME_LENGTH -> 
                "ニックネームは${User.MIN_NICKNAME_LENGTH}文字以上で入力してください"
            nickname.length > User.MAX_NICKNAME_LENGTH -> 
                "ニックネームは${User.MAX_NICKNAME_LENGTH}文字以内で入力してください"
            else -> null
        }
    }

    private fun validateBio(bio: String): String? {
        return when {
            bio.length > User.MAX_BIO_LENGTH -> 
                "自己紹介は${User.MAX_BIO_LENGTH}文字以内で入力してください"
            else -> null
        }
    }
}
