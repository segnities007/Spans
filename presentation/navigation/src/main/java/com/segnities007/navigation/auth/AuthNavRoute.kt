package com.segnities007.navigation.auth

import com.segnities007.navigation.NavRoute
import kotlinx.serialization.Serializable

// 認証フロー
@Serializable
sealed interface AuthNavRoute : NavRoute {
    @Serializable
    data object SignIn : AuthNavRoute

    @Serializable
    data object SignUp : AuthNavRoute
}
