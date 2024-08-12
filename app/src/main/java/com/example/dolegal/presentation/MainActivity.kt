package com.example.dolegal.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.dolegal.presentation.navigation.Navigation
import com.example.dolegal.presentation.ui.theme.DoLegalTheme
import com.example.dolegal.presentation.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val vModel: HomeViewModel by viewModels()
        @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            DoLegalTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val currentUser = vModel.checkUser()
                    val modifier  = Modifier
                    if (currentUser) {
                        Navigation(this,LocalContext.current,innerpadding = innerPadding,currentUser = true,modifier)
                    } else {
                        Toast.makeText(this, "Please Sign Up", Toast.LENGTH_SHORT).show()
                        Navigation(this,LocalContext.current,innerpadding = innerPadding,currentUser = false,modifier)
                    }

                }
            }
        }
    }

}
