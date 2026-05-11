package rs.ac.ftn.todotmp

import android.app.Application

class ToDoAplikacija : Application() {
    override fun onCreate() {
        super.onCreate()
        RepozitorijumZadataka.inicijalizacija(this)
    }
}
