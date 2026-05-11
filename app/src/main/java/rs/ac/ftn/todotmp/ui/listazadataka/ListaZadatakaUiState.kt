package rs.ac.ftn.todotmp.ui.listazadataka

import rs.ac.ftn.todotmp.Zadatak

data class ListaZadatakaUiState(
    val zadaci: List<Zadatak> = emptyList()
)
