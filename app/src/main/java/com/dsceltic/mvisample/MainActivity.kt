package com.dsceltic.mvisample

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.dsceltic.mvisample.feature.HomeScreen
import com.dsceltic.mvisample.ui.theme.MVITheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MVITheme {
                HomeScreen()
            }
        }
    }
}