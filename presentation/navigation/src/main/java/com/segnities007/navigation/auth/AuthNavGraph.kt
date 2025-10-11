package com.segnities007.navigation.auth

import androidx.compose.foundation.layout.padding
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
import com.segnities007.common.route.AuthNavRoute
import com.segnities007.signin.SignInScreen
import com.segnities007.signup.SignUpScreen

/**
 * 認証フローのナビゲーショングラフ
 *
 * @param modifier Modifier
 * @param navController ナビゲーションコントローラー
 * @param startDestination 初期表示画面
 * @param onAppNavigate アプリ全体のナビゲーションコールバック (認証完了時など)
 */
@Composable
fun AuthNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: AuthNavRoute = AuthNavRoute.SignIn,
    onAppNavigate: () -> Unit = {},
) {
    Scaffold{ innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = modifier.padding(innerPadding)
        ) {
            composable<AuthNavRoute.SignIn> {
                SignInScreen(
                    onAuthNavigate = { route ->
                        navController.navigate(route)
                    },
                    onAuthSuccess = onAppNavigate
                )
            }

            composable<AuthNavRoute.SignUp> {
                SignUpScreen(
                    onAuthNavigate = { route ->
                        when (route) {
                            AuthNavRoute.SignIn -> navController.popBackStack()
                            else -> navController.navigate(route)
                        }
                    },
                    onAuthSuccess = onAppNavigate
                )
            }
        }
    }
}