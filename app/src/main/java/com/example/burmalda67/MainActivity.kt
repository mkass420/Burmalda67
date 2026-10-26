package com.example.burmalda67

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.burmalda67.navigation.AppNavigation
import com.example.burmalda67.ui.theme.Burmalda67Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Burmalda67Theme {
                AppNavigation()
            }
        }
    }
}
