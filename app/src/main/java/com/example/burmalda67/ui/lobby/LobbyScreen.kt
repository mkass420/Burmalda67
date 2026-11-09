package com.example.burmalda67.ui.lobby

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.burmalda67.R
import com.example.burmalda67.data.MockData
import com.example.burmalda67.data.PokerTable
import com.example.burmalda67.ui.theme.Burmalda67Theme

@Composable
fun LobbyScreen(onOpenRoom: (String) -> Unit, modifier: Modifier = Modifier) {
    val player = MockData.player

    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.TopCenter,
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 600.dp)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Column {
                Text(
                    text = stringResource(R.string.lobby_hello, player.nickname),
                    style = MaterialTheme.typography.headlineSmall,
                )
                Text(
                    text = stringResource(R.string.lobby_chips, player.chips),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            JoinRoomCard(onJoin = onOpenRoom)

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text("Texas Hold'em", style = MaterialTheme.typography.titleMedium)
                    Text(stringResource(R.string.holdem_description), style = MaterialTheme.typography.bodyMedium)
                    Spacer(Modifier.height(12.dp))
                    Button(
                        onClick = { MockData.quickGameTable()?.let { onOpenRoom(it.code) } },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(stringResource(R.string.play))
                    }
                }
            }

            Text(stringResource(R.string.section_tables), style = MaterialTheme.typography.titleMedium)
            Card(modifier = Modifier.fillMaxWidth()) {
                MockData.tables.forEachIndexed { index, table ->
                    if (index > 0) HorizontalDivider()
                    TableRow(table) { onOpenRoom(table.code) }
                }
            }
        }
    }
}

private const val MAX_CODE_LENGTH = 10

@Composable
private fun JoinRoomCard(onJoin: (String) -> Unit) {
    var code by rememberSaveable { mutableStateOf("") }
    var error by rememberSaveable { mutableStateOf(false) }

    fun join() {
        if (code.isBlank()) error = true else onJoin(code)
    }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(stringResource(R.string.join_title), style = MaterialTheme.typography.titleMedium)

            OutlinedTextField(
                value = code,
                onValueChange = {
                    code = it.filter(Char::isLetterOrDigit).uppercase().take(MAX_CODE_LENGTH)
                    error = false
                },
                label = { Text(stringResource(R.string.join_code_label)) },
                isError = error,
                supportingText = if (error) {
                    { Text(stringResource(R.string.join_code_error)) }
                } else null,
                singleLine = true,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Characters),
                modifier = Modifier.fillMaxWidth(),
            )

            Button(onClick = { join() }, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.join_button))
            }
        }
    }
}

@Composable
private fun TableRow(table: PokerTable, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(Modifier.weight(1f)) {
            Text(table.name, style = MaterialTheme.typography.bodyLarge)
            Text(
                stringResource(R.string.blinds, table.blinds),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Text(stringResource(R.string.seats, table.seatsTaken, table.seatsTotal))
    }
}

@Preview(showBackground = true)
@Composable
private fun LobbyPreview() {
    Burmalda67Theme(dynamicColor = false) { LobbyScreen(onOpenRoom = {}) }
}
