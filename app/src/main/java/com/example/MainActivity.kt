package com.example

import android.graphics.Color as AndroidColor
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.local.QuickKartDatabase
import com.example.data.repository.RoomQuickCommerceRepository
import com.example.ui.QuickKartApp
import com.example.ui.theme.QuickKartTheme
import com.example.viewmodel.QuickKartViewModel
import com.example.viewmodel.QuickKartViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(AndroidColor.TRANSPARENT)
        )

        val db = QuickKartDatabase.getInstance(applicationContext)
        val repository = RoomQuickCommerceRepository(db)
        val factory = QuickKartViewModelFactory(repository)

        setContent {
            QuickKartTheme {
                val vm: QuickKartViewModel = viewModel(factory = factory)
                QuickKartApp(viewModel = vm)
            }
        }
    }
}
