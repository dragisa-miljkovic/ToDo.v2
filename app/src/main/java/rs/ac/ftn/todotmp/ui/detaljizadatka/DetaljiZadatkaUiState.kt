package rs.ac.ftn.todotmp.ui.detaljizadatka

data class DetaljiZadatkaUiState(
    val id: String? = null,
    val naslov: String = "",
    val opis: String = "",
    val datumMillis: Long = System.currentTimeMillis(),
    val daLiJeResen: Boolean = false
)
