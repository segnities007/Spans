package com.segnities007.signup.mvi

import com.segnities007.mvi.UiEffect

sealed interface SignUpEffect : UiEffect {
    data object NavigateToPlaza : SignUpEffect
    data object NavigateToSignIn : SignUpEffect
    data class ShowError(val message: String) : SignUpEffect
    data object ShowAvatarPicker : SignUpEffect
    data class ShowSuccess(val message: String) : SignUpEffect
}
