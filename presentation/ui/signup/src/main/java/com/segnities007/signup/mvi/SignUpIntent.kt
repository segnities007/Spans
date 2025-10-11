package com.segnities007.signup.mvi

import com.segnities007.mvi.UiIntent

sealed interface SignUpIntent : UiIntent {
    /**
     * ニックネーム変更インテント
     * 
     * @property nickname 新しいニックネーム
     */
    data class NicknameChanged(val nickname: String) : SignUpIntent

    /**
     * 自己紹介変更インテント
     * 
     * @property bio 新しい自己紹介
     */
    data class BioChanged(val bio: String) : SignUpIntent

    /**
     * アバター画像選択インテント
     * 
     * @property uri 選択された画像のURI
     */
    data class AvatarSelected(val uri: String?) : SignUpIntent

    /**
     * アバター画像削除インテント
     */
    data object AvatarRemoved : SignUpIntent

    /**
     * サインアップボタンクリックインテント
     */
    data object SignUpClicked : SignUpIntent

    /**
     * サインインへ移動インテント
     */
    data object NavigateToSignIn : SignUpIntent

    /**
     * エラーリトライインテント
     */
    data object RetryClicked : SignUpIntent
}
