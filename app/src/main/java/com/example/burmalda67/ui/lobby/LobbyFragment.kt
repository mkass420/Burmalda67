package com.example.burmalda67.ui.lobby

import androidx.compose.runtime.Composable
import com.example.burmalda67.ui.ComposeFragment

class LobbyFragment : ComposeFragment() {

    var onOpenRoom: (String) -> Unit = {}

    @Composable
    override fun Content() {
        LobbyScreen(onOpenRoom = { onOpenRoom(it) })
    }
}
