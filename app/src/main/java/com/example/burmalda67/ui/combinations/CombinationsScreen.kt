package com.example.burmalda67.ui.combinations

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.burmalda67.R
import com.example.burmalda67.data.HandCombinations

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CombinationsScreen(modifier: Modifier = Modifier) {
    val adapter = remember { CombinationsAdapter(HandCombinations.all) }

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.combinations_title)) },
            )
        },
    ) { innerPadding ->
        AndroidView(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            factory = { context ->
                RecyclerView(context).apply {
                    layoutManager = LinearLayoutManager(context)
                    this.adapter = adapter
                    clipToPadding = false
                    setPadding(0, 8.dp(context), 0, 8.dp(context))
                }
            },
        )
    }
}

private fun Int.dp(context: android.content.Context): Int =
    (this * context.resources.displayMetrics.density).toInt()
