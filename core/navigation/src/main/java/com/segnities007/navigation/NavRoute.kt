package com.segnities007.navigation

sealed interface NavRoute{
    data object Auth: NavRoute
    data object Plaza: NavRoute
    data object Setting: NavRoute
    data object Timeline: NavRoute
}