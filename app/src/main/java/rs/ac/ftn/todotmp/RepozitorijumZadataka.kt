package rs.ac.ftn.todotmp

import android.content.Context
import androidx.room.Room
import kotlinx.coroutines.flow.Flow
import rs.ac.ftn.todotmp.bazapodataka.ZadaciBazaPodataka

private const val IME_BAZE = "zadatak-bazapodataka"

class RepozitorijumZadataka private constructor(context: Context) {
    private val bazaPodataka = Room.databaseBuilder(
        context.applicationContext,
        ZadaciBazaPodataka::class.java,
        IME_BAZE
    ).build()

    fun getZadaci(): Flow<List<Zadatak>> = bazaPodataka.zadatakDao().getZadaci()

    suspend fun getZadatak(id: String): Zadatak? = bazaPodataka.zadatakDao().getZadatak(id)

    suspend fun dodajZadatak(zadatak: Zadatak) = bazaPodataka.zadatakDao().dodajZadatak(zadatak)

    suspend fun azurirajZadatak(zadatak: Zadatak) = bazaPodataka.zadatakDao().azurirajZadatak(zadatak)

    suspend fun obrisiZadatak(zadatak: Zadatak) = bazaPodataka.zadatakDao().obrisiZadatak(zadatak)

    suspend fun brojZadataka(): Int = bazaPodataka.zadatakDao().brojZadataka()

    companion object {
        private var INSTANCA: RepozitorijumZadataka? = null

        fun inicijalizacija(context: Context) {
            if (INSTANCA == null) {
                INSTANCA = RepozitorijumZadataka(context)
            }
        }

        fun get(): RepozitorijumZadataka {
            return INSTANCA ?: error("RepozitorijumZadataka nije inicijalizovan")
        }
    }
}
