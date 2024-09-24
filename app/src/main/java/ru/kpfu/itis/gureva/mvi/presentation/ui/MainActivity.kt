package ru.kpfu.itis.gureva.mvi.presentation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.content.MediaType.Companion.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.kpfu.itis.gureva.mvi.presentation.ui.screen.main.MainScreen
import ru.kpfu.itis.gureva.mvi.presentation.ui.theme.MviTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContent {
            MviTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun FruitNavHost(
    navHostController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navHostController,
        startDestination = "Home"
    ) {
        composable(route = "Home") {

        }
    }
}

@Preview
@Composable
fun N() {
    MviTheme {
        Surface(Modifier.fillMaxSize()) {
            Column {
                Text(text = "mkenfe", style = MaterialTheme.typography.headlineSmall)
                Text(text = "mkenfe", style = MaterialTheme.typography.titleMedium)
            }

        }
    }
}
