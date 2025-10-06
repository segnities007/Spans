package com.segnities007.navigation.auth

import kotlinx.serialization.Serializable

// 認証フロー
@Serializable
sealed interface AuthNavRoute {
    @Serializable
    data object SignIn : AuthNavRoute

    @Serializable
    data object SignUp : AuthNavRoute
}
