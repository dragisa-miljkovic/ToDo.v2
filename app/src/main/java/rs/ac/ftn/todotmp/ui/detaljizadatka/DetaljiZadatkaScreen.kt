package rs.ac.ftn.todotmp.ui.detaljizadatka

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
    fun DetaljiZadatkaScreen(
    uiState: DetaljiZadatkaUiState,
    onNazadClick: () -> Unit,
    onNaslovChange: (String) -> Unit,
    onOpisChange: (String) -> Unit,
    onDatumChange: (Long) -> Unit,
    onResenChange: (Boolean) -> Unit,
    onSacuvajClick: () -> Unit,
    onObrisiClick: () -> Unit
) {
    var prikaziDatumDialog by remember { mutableStateOf(false) }
    val formatter = remember { SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (uiState.id == null) "Нови задатак" else "Детаљи") },
                navigationIcon = {
                    IconButton(onClick = onNazadClick) {
                        Text("←")
                    }
                },
                actions = {
                    if (uiState.id != null) {
                        IconButton(onClick = onObrisiClick) {
                            Text("✕")
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = uiState.naslov,
                onValueChange = onNaslovChange,
                label = { Text("Наслов") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = uiState.opis,
                onValueChange = onOpisChange,
                label = { Text("Опис") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            )

            Button(
                onClick = { prikaziDatumDialog = true },
                modifier = Modifier.padding(top = 12.dp)
            ) {
                Text("Датум: ${formatter.format(Date(uiState.datumMillis))}")
            }

            Row(modifier = Modifier.padding(top = 8.dp)) {
                Checkbox(checked = uiState.daLiJeResen, onCheckedChange = onResenChange)
                Text("Завршен", modifier = Modifier.padding(top = 12.dp))
            }

            Button(
                onClick = onSacuvajClick,
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Сачувај")
            }
        }
    }

    if (prikaziDatumDialog) {
        val datePickerState = androidx.compose.material3.rememberDatePickerState(
            initialSelectedDateMillis = uiState.datumMillis
        )

        DatePickerDialog(
            onDismissRequest = { prikaziDatumDialog = false },
            confirmButton = {
                Button(
                    onClick = {
                        datePickerState.selectedDateMillis?.let(onDatumChange)
                        prikaziDatumDialog = false
                    }
                ) {
                    Text("Потврди")
                }
            },
            dismissButton = {
                Button(onClick = { prikaziDatumDialog = false }) {
                    Text("Откажи")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}
