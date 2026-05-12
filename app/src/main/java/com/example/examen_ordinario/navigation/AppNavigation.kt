package com.example.examen_ordinario.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.examen_ordinario.pantallas.Pantalla_2
import com.example.examen_ordinario.pantallas.Pantalla_Inicial

object Rutas {
    const val PANTALLA_INICIAL = "pantallaInicial"
    const val PANTALLA_2 = "pantalla2"
}

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Rutas.PANTALLA_INICIAL,
        modifier = modifier
    ) {
        composable(Rutas.PANTALLA_INICIAL) {
            Pantalla_Inicial(navController = navController)
        }
        composable(Rutas.PANTALLA_2) {
            Pantalla_2(navController = navController)
        }
    }
}
