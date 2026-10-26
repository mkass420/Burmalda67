package com.example.burmalda67.ui.lobby

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.burmalda67.R
import com.example.burmalda67.data.MockData
import com.example.burmalda67.data.PokerTable
import com.example.burmalda67.RoomActivity
import com.example.burmalda67.ui.theme.Burmalda67Theme

@Composable
fun LobbyScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val player = MockData.player

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .safeDrawingPadding(),
        contentAlignment = Alignment.TopCenter,
    ) {
        // на узких экранах кнопки ставим друг под другом
        val narrow = maxWidth < 360.dp

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

            JoinRoomCard(
                narrow = narrow,
                onExplicit = { code -> openRoom(context, code) },
                onImplicit = { code -> openRoomByLink(context, code) },
            )

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text("Texas Hold'em", style = MaterialTheme.typography.titleMedium)
                    Text(stringResource(R.string.holdem_description), style = MaterialTheme.typography.bodyMedium)
                    Spacer(Modifier.height(12.dp))
                    Button(
                        onClick = { openRoom(context, "HOLD01") },
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
                    TableRow(table) { openRoom(context, table.code) }
                }
            }
        }
    }
}

// явный интент: сразу указываем, какую активность открыть
private fun openRoom(context: Context, code: String) {
    val intent = Intent(context, RoomActivity::class.java)
    intent.putExtra("code", code)
    context.startActivity(intent)
}

// неявный интент: только действие и ссылка, нужную активность система найдёт по intent-filter
private fun openRoomByLink(context: Context, code: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("burmalda://room/$code"))
    context.startActivity(intent)
}

@Composable
private fun JoinRoomCard(
    narrow: Boolean,
    onExplicit: (String) -> Unit,
    onImplicit: (String) -> Unit,
) {
    var code by rememberSaveable { mutableStateOf("") }
    var error by rememberSaveable { mutableStateOf(false) }

    fun open(action: (String) -> Unit) {
        if (code.isBlank()) error = true else action(code)
    }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(stringResource(R.string.join_title), style = MaterialTheme.typography.titleMedium)

            OutlinedTextField(
                value = code,
                onValueChange = {
                    code = it.filter(Char::isLetterOrDigit).uppercase().take(6)
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

            if (narrow) {
                Button(onClick = { open(onExplicit) }, modifier = Modifier.fillMaxWidth()) {
                    Text(stringResource(R.string.join_explicit))
                }
                OutlinedButton(onClick = { open(onImplicit) }, modifier = Modifier.fillMaxWidth()) {
                    Text(stringResource(R.string.join_implicit))
                }
            } else {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = { open(onExplicit) }, modifier = Modifier.weight(1f)) {
                        Text(stringResource(R.string.join_explicit))
                    }
                    OutlinedButton(onClick = { open(onImplicit) }, modifier = Modifier.weight(1f)) {
                        Text(stringResource(R.string.join_implicit))
                    }
                }
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
    Burmalda67Theme(dynamicColor = false) { LobbyScreen() }
}
