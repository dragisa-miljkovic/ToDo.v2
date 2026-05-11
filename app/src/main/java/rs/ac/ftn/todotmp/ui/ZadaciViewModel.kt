package rs.ac.ftn.todotmp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import rs.ac.ftn.todotmp.RepozitorijumZadataka
import rs.ac.ftn.todotmp.Zadatak
import rs.ac.ftn.todotmp.ui.detaljizadatka.DetaljiZadatkaUiState
import rs.ac.ftn.todotmp.ui.listazadataka.ListaZadatakaUiState
import java.util.UUID

class ZadaciViewModel : ViewModel() {
    private val repozitorijum = RepozitorijumZadataka.get()

    private val _uiState = MutableStateFlow(ListaZadatakaUiState())
    val uiState: StateFlow<ListaZadatakaUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            if (repozitorijum.brojZadataka() == 0) {
                pocetniZadaci().forEach { repozitorijum.dodajZadatak(it) }
            }

            repozitorijum.getZadaci().collect { zadaci ->
                _uiState.value = ListaZadatakaUiState(zadaci = zadaci)
            }
        }
    }

    fun promeniStatus(zadatakId: String) {
        viewModelScope.launch {
            val zadatak = repozitorijum.getZadatak(zadatakId) ?: return@launch
            repozitorijum.azurirajZadatak(zadatak.copy(daLiJeResen = !zadatak.daLiJeResen))
        }
    }

    fun nadjiZadatak(zadatakId: String): Zadatak? =
        _uiState.value.zadaci.firstOrNull { it.id == zadatakId }

    fun sacuvaj(detalji: DetaljiZadatkaUiState) {
        if (detalji.naslov.isBlank()) return

        viewModelScope.launch {
            val zadatak = Zadatak(
                id = detalji.id ?: UUID.randomUUID().toString(),
                naslov = detalji.naslov,
                opis = detalji.opis,
                datumMillis = detalji.datumMillis,
                daLiJeResen = detalji.daLiJeResen
            )

            if (detalji.id == null) {
                repozitorijum.dodajZadatak(zadatak)
            } else {
                repozitorijum.azurirajZadatak(zadatak)
            }
        }
    }

    fun obrisi(zadatakId: String) {
        viewModelScope.launch {
            val zadatak = repozitorijum.getZadatak(zadatakId) ?: return@launch
            repozitorijum.obrisiZadatak(zadatak)
        }
    }

    private fun pocetniZadaci(): List<Zadatak> {
        val danas = System.currentTimeMillis()
        val dan = 24 * 60 * 60 * 1000L

        return listOf(
        Zadatak(
            id = "1",
            naslov = "Припремити вежбе",
            opis = "Проверити примере и задатке за термин.",
            datumMillis = danas,
            daLiJeResen = false
        ),
        Zadatak(
            id = "2",
            naslov = "Обновити Compose state",
            opis = "remember, rememberSaveable и state hoisting.",
            datumMillis = danas + dan,
            daLiJeResen = true
        ),
        Zadatak(
            id = "3",
            naslov = "Написати домаћи",
            opis = "Задатак из ToDo блока.",
            datumMillis = danas + 2 * dan,
            daLiJeResen = false
        )
    )
    }
}
