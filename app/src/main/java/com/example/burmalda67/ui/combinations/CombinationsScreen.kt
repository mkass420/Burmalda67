package com.example.burmalda67.ui.combinations

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.burmalda67.R
import com.example.burmalda67.data.HandCombination
import com.example.burmalda67.data.HandCombinations
import com.example.burmalda67.ui.theme.Burmalda67Theme
import com.example.burmalda67.ui.theme.CardBorder
import com.example.burmalda67.ui.theme.CardFace
import com.example.burmalda67.ui.theme.SuitBlack
import com.example.burmalda67.ui.theme.SuitRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CombinationsScreen(modifier: Modifier = Modifier) {
    // ранги раскрытых элементов; по клику раскрывается описание
    var expanded by rememberSaveable { mutableStateOf(emptySet<Int>()) }

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0),
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.combinations_title)) })
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(vertical = 8.dp),
        ) {
            items(HandCombinations.all, key = { it.rank }) { item ->
                val isExpanded = item.rank in expanded
                CombinationItem(
                    item = item,
                    expanded = isExpanded,
                    onClick = { expanded = if (isExpanded) expanded - item.rank else expanded + item.rank },
                )
            }
        }
    }
}

@Composable
private fun CombinationItem(item: HandCombination, expanded: Boolean, onClick: () -> Unit) {
    val chevronRotation by animateFloatAsState(if (expanded) 180f else 0f, label = "chevron")

    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        item.rank.toString(),
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }
                Text(
                    item.name,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 12.dp),
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    item.probability,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.secondaryContainer, RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                )
                Icon(
                    Icons.Filled.ExpandMore,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .size(24.dp)
                        .rotate(chevronRotation),
                )
            }

            Row(
                modifier = Modifier.padding(top = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                item.cards.forEach { card ->
                    PlayingCard(
                        card,
                        Modifier
                            .weight(1f, fill = false)
                            .widthIn(max = 40.dp)
                            .fillMaxWidth(),
                    )
                }
            }

            AnimatedVisibility(visible = expanded) {
                Text(
                    item.description,
                    modifier = Modifier.padding(top = 12.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

// мини-карта: белая «рубашка», красные черви/бубны, чёрные пики/трефы
@Composable
private fun PlayingCard(card: String, modifier: Modifier = Modifier) {
    val isRed = card.endsWith("♥") || card.endsWith("♦")
    val shape = RoundedCornerShape(6.dp)
    Box(
        modifier = modifier
            .height(48.dp)
            .background(CardFace, shape)
            .border(1.dp, CardBorder, shape),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            card,
            color = if (isRed) SuitRed else SuitBlack,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CombinationsPreview() {
    Burmalda67Theme(dynamicColor = false) { CombinationsScreen() }
}
