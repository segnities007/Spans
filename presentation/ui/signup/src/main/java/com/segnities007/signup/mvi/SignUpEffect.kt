package com.segnities007.signup.mvi

import com.segnities007.mvi.UiEffect

sealed interface SignUpEffect : UiEffect {
    /**
     * サインアップ成功時の効果
     * Plaza画面へナビゲート
     */
    data object NavigateToPlaza : SignUpEffect

    /**
     * サインイン画面へナビゲート
     */
    data object NavigateToSignIn : SignUpEffect

    /**
     * エラーメッセージ表示
     * 
     * @property message 表示するエラーメッセージ
     */
    data class ShowError(val message: String) : SignUpEffect

    /**
     * アバター画像選択ダイアログ表示
     */
    data object ShowAvatarPicker : SignUpEffect

    /**
     * 成功メッセージ表示
     * 
     * @property message 表示する成功メッセージ
     */
    data class ShowSuccess(val message: String) : SignUpEffect
}
