package com.example.burmalda67

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar

// Комната (пока заглушка). Открывается явным интентом или по ссылке burmalda://room/<код>
class RoomActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_room)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener { finish() }

        val roomText = findViewById<TextView>(R.id.roomText)

        // если активность открыли по ссылке, код берём из uri, иначе из extras
        val uri = intent?.data
        if (uri != null) {
            val code = uri.lastPathSegment
            roomText.text = "$code (неявный Intent)"
        } else {
            val code = intent.getStringExtra("code")
            roomText.text = "$code (Intent)"
        }
    }
}
