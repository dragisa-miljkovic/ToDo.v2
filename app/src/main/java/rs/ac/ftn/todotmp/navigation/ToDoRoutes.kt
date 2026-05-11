package rs.ac.ftn.todotmp.navigation

object ToDoRoutes {
    const val LISTA = "lista_zadataka"
    const val NOVI = "novi_zadatak"
    const val DETALJI = "detalji_zadatka/{zadatakId}"

    fun detaljiRoute(zadatakId: String): String = "detalji_zadatka/$zadatakId"
}
