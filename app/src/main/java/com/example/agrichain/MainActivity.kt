package com.example.agrichain

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.agrichain.data.repository.ProductRepository
import com.example.agrichain.ui.navigation.AgriChainNavGraph
import com.example.agrichain.ui.theme.AgriChainTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        // Initialize the persistent product database.
        ProductRepository.initialize(applicationContext)

        setContent {
            AgriChainApp()
        }
    }
}

@Composable
private fun AgriChainApp() {

    AgriChainTheme {

        Surface(
            modifier = Modifier.fillMaxSize()
        ) {

            val navController = rememberNavController()

            AgriChainNavGraph(
                navController = navController
            )
        }
    }
}