package com.segnities007.spans

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.segnities007.navigation.NavRoute

@Composable
internal fun Navigation(){
    val navController = rememberNavController()
    var topBar by remember{ mutableStateOf<@Composable () -> Unit>({}) }
    var bottomBar by remember{ mutableStateOf<@Composable () -> Unit>({}) }
    var fab by remember{ mutableStateOf<@Composable () -> Unit>({}) }
    val currentRoute by remember{ mutableStateOf<NavRoute>(NavRoute.Auth) }
    val updateTopBar: (@Composable () -> Unit) -> Unit = { topBar = it }
    val updateBottomBar: (@Composable () -> Unit) -> Unit = { bottomBar = it }
    val updateFab: (@Composable () -> Unit) -> Unit = { fab = it }

    NavigationUi(
        topBar = topBar,
        bottomBar = bottomBar,
        fab = fab
    ){
        NavHost(navController, startDestination = currentRoute){
            composable<NavRoute.Auth>{

            }
            composable<NavRoute.Plaza>{

            }
            composable<NavRoute.Setting>{

            }
            composable<NavRoute.Timeline>{

            }
        }
    }
}

@Composable
private fun NavigationUi(
    topBar: @Composable () -> Unit,
    bottomBar: @Composable () -> Unit,
    fab: @Composable () -> Unit,
    content: @Composable (PaddingValues) -> Unit
){
    Scaffold(
        topBar = topBar,
        bottomBar = bottomBar,
        floatingActionButton = fab
    ){ innerPadding ->
        content(innerPadding)
    }
}
