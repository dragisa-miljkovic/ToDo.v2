package rs.ac.ftn.todotmp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = TealPrimary,
    secondary = TealSecondary,
    background = CreamBackground
)

private val DarkColors = darkColorScheme(
    primary = TealSecondary,
    secondary = TealPrimary
)

@Composable
fun ToDotmpTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = Typography,
        content = content
    )
}
