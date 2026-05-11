package rs.ac.ftn.todotmp.bazapodataka

import androidx.room.Database
import androidx.room.RoomDatabase
import rs.ac.ftn.todotmp.Zadatak

@Database(entities = [Zadatak::class], version = 1, exportSchema = false)
abstract class ZadaciBazaPodataka : RoomDatabase() {
    abstract fun zadatakDao(): ZadatakDao
}
