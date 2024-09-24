package ru.kpfu.itis.gureva.mvi.presentation.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.content.MediaType.Companion.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.edit
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.kpfu.itis.gureva.mvi.data.database.repository.GroupRepository
import ru.kpfu.itis.gureva.mvi.presentation.ui.screen.group.GroupScreen
import ru.kpfu.itis.gureva.mvi.presentation.ui.screen.main.MainScreen
import ru.kpfu.itis.gureva.mvi.presentation.ui.theme.MviTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var groupRepository: GroupRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        appInit()
        setContent {
            MviTheme {
                AppNavHost()
            }
        }
    }

    private fun appInit() {
        val shp = getSharedPreferences("", MODE_PRIVATE)

        if (!shp.getBoolean("init", false)) {

            lifecycleScope.launch {
                try {
                    groupRepository.init()
                    shp.edit {
                        putBoolean("init", true)
                    }
                } catch (_: Exception) {}
            }
        }
    }
}

@Composable
fun AppNavHost(
    navHostController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navHostController,
        startDestination = "Home"
    ) {
        composable(route = "Home") {
            MainScreen(navigateToGroup = {
                navHostController.navigate(route = "Group/${it ?: 1}")
            })
        }

        composable(
            route = "Group/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStack ->
            Log.e("args", backStack.arguments?.getInt("id").toString())
            GroupScreen(backStack.arguments?.getInt("id") ?: 1)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun N() {
    MviTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            var showBottomSheet by remember { mutableStateOf(false) }
            val sheetState = rememberModalBottomSheetState(
                skipPartiallyExpanded = false,
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Button(
                    onClick = { showBottomSheet = true }
                ) {
                    Text("Display partial bottom sheet")
                }

                if (showBottomSheet) {
                    ModalBottomSheet(
                        modifier = Modifier.fillMaxHeight(),
                        sheetState = sheetState,
                        onDismissRequest = { showBottomSheet = false }
                    ) {
                        Text(
                            "Swipe up to open sheet. Swipe down to dismiss.",
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}
