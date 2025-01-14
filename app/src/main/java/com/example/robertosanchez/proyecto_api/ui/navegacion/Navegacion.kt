package com.example.robertosanchez.proyecto_api.ui.navegacion

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.robertosanchez.proyecto_api.ui.screens.buscarScreen.BuscarScreen
import com.example.robertosanchez.proyecto_api.ui.screens.detailScreen.DetailScreen
import com.example.robertosanchez.proyecto_api.ui.screens.inicioScreen.InicioScreen
import com.example.robertosanchez.proyecto_api.ui.screens.listaScreen.ListaScreen
import com.example.robertosanchez.proyecto_api.ui.screens.listaScreen.ListaViewModel

@Composable
fun Navegacion() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Inicio) {
        composable<Inicio> {
            InicioScreen() {
                navController.navigate(Buscar)
            }
        }

        composable<Buscar> {
            BuscarScreen { cantante ->
                navController.navigate(Lista(cantante))
            }
        }

        composable<Lista> {backStackEntry ->
            val cantante = backStackEntry.arguments?.getString("cantante") ?: ""
            val viewModel = ListaViewModel(cantante)

            ListaScreen(cantante, viewModel,
                onBack = {navController.popBackStack() },
                navigateToDetail = { id, cantante ->
                navController.navigate(Detail(id, cantante))
            })
        }

        composable<Detail> { backStackEntry ->
            val detail = backStackEntry.toRoute<Detail>()
            val id = detail.id
            val cantante = backStackEntry.arguments?.getString("cantante") ?: ""
            DetailScreen(id, cantante,
                    onBack = { navController.popBackStack() },
                    navigateToList = { navController.navigate(Lista(cantante)) }
            )
        }
    }
}