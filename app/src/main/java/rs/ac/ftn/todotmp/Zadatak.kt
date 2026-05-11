package rs.ac.ftn.todotmp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Zadatak(
    @PrimaryKey val id: String,
    val naslov: String,
    val opis: String,
    val datumMillis: Long,
    val daLiJeResen: Boolean
)
