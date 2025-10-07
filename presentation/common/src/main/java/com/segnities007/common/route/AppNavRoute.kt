package com.segnities007.common.route

import kotlinx.serialization.Serializable

/**
 * アプリ全体のトップレベルルート
 */
@Serializable
sealed interface AppNavRoute {
    @Serializable
    data object Auth : AppNavRoute
    @Serializable
    data object Hub : AppNavRoute
}

/**
 * 認証フローのルート
 */
@Serializable
sealed interface AuthNavRoute : AppNavRoute {
    @Serializable
    data object SignIn : AuthNavRoute

    @Serializable
    data object SignUp : AuthNavRoute
}

/**
 * ハブ(メインアプリ)フローのルート
 */
@Serializable
sealed interface HubNavRoute : AppNavRoute {
    @Serializable
    data object Plaza : HubNavRoute
    @Serializable
    data object Timeline : HubNavRoute
    @Serializable
    data object Search : HubNavRoute
    @Serializable
    data object Profile : HubNavRoute
    @Serializable
    data object Settings : HubNavRoute
}