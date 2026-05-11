package rs.ac.ftn.todotmp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import rs.ac.ftn.todotmp.ui.ZadaciViewModel
import rs.ac.ftn.todotmp.ui.detaljizadatka.DetaljiZadatkaScreen
import rs.ac.ftn.todotmp.ui.detaljizadatka.DetaljiZadatkaUiState
import rs.ac.ftn.todotmp.ui.listazadataka.ListaZadatakaScreen

@Composable
fun ToDoNavHost() {
    val navController = rememberNavController()
    val viewModel: ZadaciViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    NavHost(navController = navController, startDestination = ToDoRoutes.LISTA) {
        composable(ToDoRoutes.LISTA) {
            ListaZadatakaScreen(
                uiState = uiState,
                onZadatakClick = { zadatakId -> navController.navigate(ToDoRoutes.detaljiRoute(zadatakId)) },
                onDodajClick = { navController.navigate(ToDoRoutes.NOVI) },
                onPromeniStatus = viewModel::promeniStatus
            )
        }

        composable(ToDoRoutes.NOVI) {
            var detailsState by remember {
                mutableStateOf(DetaljiZadatkaUiState())
            }
            DetaljiZadatkaScreen(
                uiState = detailsState,
                onNazadClick = { navController.navigateUp() },
                onNaslovChange = { detailsState = detailsState.copy(naslov = it) },
                onOpisChange = { detailsState = detailsState.copy(opis = it) },
                onDatumChange = { detailsState = detailsState.copy(datumMillis = it) },
                onResenChange = { detailsState = detailsState.copy(daLiJeResen = it) },
                onSacuvajClick = {
                    viewModel.sacuvaj(detailsState)
                    detailsState = DetaljiZadatkaUiState()
                    navController.navigateUp()
                },
                onObrisiClick = {
                    navController.navigateUp()
                }
            )
        }

        composable(
            route = ToDoRoutes.DETALJI,
            arguments = listOf(navArgument("zadatakId") { type = NavType.StringType })
        ) { entry ->
            val zadatakId = entry.arguments?.getString("zadatakId").orEmpty()
            val zadatak = viewModel.nadjiZadatak(zadatakId)
            var detailsState by remember(zadatakId, zadatak) {
                mutableStateOf(
                    DetaljiZadatkaUiState(
                        id = zadatak?.id,
                        naslov = zadatak?.naslov.orEmpty(),
                        opis = zadatak?.opis.orEmpty(),
                        datumMillis = zadatak?.datumMillis ?: System.currentTimeMillis(),
                        daLiJeResen = zadatak?.daLiJeResen ?: false
                    )
                )
            }

            DetaljiZadatkaScreen(
                uiState = detailsState,
                onNazadClick = { navController.navigateUp() },
                onNaslovChange = { detailsState = detailsState.copy(naslov = it) },
                onOpisChange = { detailsState = detailsState.copy(opis = it) },
                onDatumChange = { detailsState = detailsState.copy(datumMillis = it) },
                onResenChange = { detailsState = detailsState.copy(daLiJeResen = it) },
                onSacuvajClick = {
                    viewModel.sacuvaj(detailsState)
                    navController.navigateUp()
                },
                onObrisiClick = {
                    detailsState.id?.let { viewModel.obrisi(it) }
                    navController.navigateUp()
                }
            )
        }
    }
}
