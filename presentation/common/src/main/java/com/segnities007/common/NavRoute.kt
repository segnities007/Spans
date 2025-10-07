package com.segnities007.common

import kotlinx.serialization.Serializable

/**
 * アプリ全体のトップレベルルート
 */
@Serializable
sealed interface AppNavRoute {
    @Serializable
    data class Auth(val route: AuthRoute? = null) : AppNavRoute
    
    @Serializable
    data class Hub(val route: HubRoute? = null) : AppNavRoute
}

/**
 * 認証フローのルート
 */
@Serializable
sealed interface AuthRoute : NavRoute {
    @Serializable
    data object SignIn : AuthRoute

    @Serializable
    data object SignUp : AuthRoute
}

/**
 * ハブ(メインアプリ)フローのルート
 */
@Serializable
sealed interface HubRoute : NavRoute {
    @Serializable
    data object Plaza : HubRoute

    @Serializable
    data object Timeline : HubRoute

    @Serializable
    data object Search : HubRoute

    @Serializable
    data object Profile : HubRoute

    @Serializable
    data object Settings : HubRoute
}