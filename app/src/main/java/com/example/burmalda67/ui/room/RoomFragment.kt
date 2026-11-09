package com.example.burmalda67.ui.room

import androidx.compose.runtime.Composable
import com.example.burmalda67.ui.ComposeFragment

class RoomFragment : ComposeFragment() {

    @Composable
    override fun Content() {
        RoomScreen(
            code = requireArguments().getString(ARG_CODE).orEmpty(),
            onBack = { requireActivity().onBackPressedDispatcher.onBackPressed() },
        )
    }

    companion object {
        const val ARG_CODE = "code"
    }
}
