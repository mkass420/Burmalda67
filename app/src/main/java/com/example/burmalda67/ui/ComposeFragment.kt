package com.example.burmalda67.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.fragment.app.Fragment
import androidx.fragment.compose.content
import com.example.burmalda67.ui.theme.Burmalda67Theme

abstract class ComposeFragment : Fragment() {

    @Composable
    protected abstract fun Content()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = content {
        Burmalda67Theme {
            Box(Modifier.focusable()) {
                Content()
            }
        }
    }
}
