package com.example.burmalda67.ui.room

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.burmalda67.R
import com.example.burmalda67.data.MockData
import com.example.burmalda67.data.PokerTable
import com.example.burmalda67.ui.theme.Burmalda67Theme

// Комната: данные стола или заглушка, если кода нет
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomScreen(code: String, onBack: () -> Unit, modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.room_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.room_back),
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        val table = MockData.findTable(code)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
        ) {
            if (table != null) TableInfo(table) else NotFound(code, onBack)
        }
    }
}

@Composable
private fun TableInfo(table: PokerTable) {
    Text(table.name, style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center)
    Text(stringResource(R.string.room_code, table.code), style = MaterialTheme.typography.bodyMedium)
    Text(stringResource(R.string.room_blinds, table.blinds), style = MaterialTheme.typography.bodyLarge)
    Text(
        stringResource(R.string.room_seats, table.seatsTaken, table.seatsTotal),
        style = MaterialTheme.typography.bodyLarge,
    )
    Text(
        stringResource(R.string.room_waiting),
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

// заглушка для кода, которого нет среди столов
@Composable
private fun NotFound(code: String, onBack: () -> Unit) {
    Icon(
        Icons.Filled.SearchOff,
        contentDescription = null,
        modifier = Modifier.size(64.dp),
        tint = MaterialTheme.colorScheme.onSurfaceVariant,
    )
    Text(stringResource(R.string.room_not_found_title), style = MaterialTheme.typography.headlineSmall)
    Text(
        stringResource(R.string.room_not_found_text, code),
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center,
    )
    Button(onClick = onBack) { Text(stringResource(R.string.room_to_lobby)) }
}

@Preview(showBackground = true)
@Composable
private fun RoomPreview() {
    Burmalda67Theme(dynamicColor = false) { RoomScreen(code = "ACE777", onBack = {}) }
}

@Preview(showBackground = true)
@Composable
private fun RoomNotFoundPreview() {
    Burmalda67Theme(dynamicColor = false) { RoomScreen(code = "NOPE", onBack = {}) }
}
