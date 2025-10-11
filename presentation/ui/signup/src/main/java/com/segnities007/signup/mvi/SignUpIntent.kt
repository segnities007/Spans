package com.segnities007.signup.mvi

import com.segnities007.mvi.UiIntent

sealed interface SignUpIntent : UiIntent {
    data class NicknameChanged(val nickname: String) : SignUpIntent
    data class BioChanged(val bio: String) : SignUpIntent
    data class AvatarSelected(val uri: String?) : SignUpIntent
    data object AvatarRemoved : SignUpIntent
    data object SignUpClicked : SignUpIntent
    data object NavigateToSignIn : SignUpIntent
    data object RetryClicked : SignUpIntent
}