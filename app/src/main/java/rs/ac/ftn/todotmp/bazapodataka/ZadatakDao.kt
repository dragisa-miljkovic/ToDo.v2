package rs.ac.ftn.todotmp.bazapodataka

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import rs.ac.ftn.todotmp.Zadatak

@Dao
interface ZadatakDao {
    @Query("SELECT * FROM zadatak ORDER BY datumMillis ASC")
    fun getZadaci(): Flow<List<Zadatak>>

    @Query("SELECT * FROM zadatak WHERE id = :id")
    suspend fun getZadatak(id: String): Zadatak?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun dodajZadatak(zadatak: Zadatak)

    @Update
    suspend fun azurirajZadatak(zadatak: Zadatak)

    @Delete
    suspend fun obrisiZadatak(zadatak: Zadatak)

    @Query("SELECT COUNT(*) FROM zadatak")
    suspend fun brojZadataka(): Int
}
