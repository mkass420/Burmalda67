package com.example.burmalda67

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.fragment.app.FragmentActivity
import com.example.burmalda67.ui.MainScreen
import com.example.burmalda67.ui.theme.Burmalda67Theme

class MainActivity : FragmentActivity() {
    private var deepLinkRoom by mutableStateOf<String?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        if (savedInstanceState == null) deepLinkRoom = intent.roomCode()

        setContent {
            Burmalda67Theme {
                MainScreen(
                    deepLinkRoom = deepLinkRoom,
                    onDeepLinkHandled = { deepLinkRoom = null },
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        intent.roomCode()?.let { deepLinkRoom = it }
    }
}

private fun Intent.roomCode(): String? =
    data?.takeIf { it.scheme == "burmalda" && it.host == "room" }?.lastPathSegment
