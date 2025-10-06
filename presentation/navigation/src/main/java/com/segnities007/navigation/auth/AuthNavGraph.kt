package com.segnities007.navigation.auth

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AuthNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: AuthNavRoute = AuthNavRoute.SignIn,
) {
    var topBar by remember { mutableStateOf<@Composable () -> Unit>({}) }
    var bottomBar by remember { mutableStateOf<@Composable () -> Unit>({}) }
    var floatingActionButton by remember { mutableStateOf<@Composable () -> Unit>({}) }
    val updateTopBar: (@Composable () -> Unit) -> Unit = { topBar = it }
    val updateBottomBar: (@Composable () -> Unit) -> Unit = { bottomBar = it }
    val updateFloatingActionButton: (@Composable () -> Unit) -> Unit = { floatingActionButton = it }

    Scaffold(
        topBar = topBar,
        bottomBar = bottomBar,
        floatingActionButton = floatingActionButton,
    ){ innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = modifier
        ) {
            composable<AuthNavRoute.SignIn> {
                // TODO: auth:ui:SignInScreenをここで呼び出す
            }

            composable<AuthNavRoute.SignUp> {
                // TODO: auth:ui:SignUpScreenをここで呼び出す
            }
        }
    }
}