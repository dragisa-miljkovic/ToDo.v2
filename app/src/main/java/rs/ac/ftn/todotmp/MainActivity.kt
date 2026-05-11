package rs.ac.ftn.todotmp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import rs.ac.ftn.todotmp.ui.ToDoApp
import rs.ac.ftn.todotmp.ui.theme.ToDotmpTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ToDotmpTheme {
                ToDoApp()
            }
        }
    }
}
