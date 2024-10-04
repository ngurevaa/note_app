package ru.kpfu.itis.gureva.mvi.presentation.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.content.MediaType.Companion.Text
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.edit
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph
import androidx.navigation.navArgument
import androidx.navigation.toRoute
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.kpfu.itis.gureva.mvi.R
import ru.kpfu.itis.gureva.mvi.data.database.repository.GroupRepository
import ru.kpfu.itis.gureva.mvi.presentation.ui.screen.calendar.Calendar
import ru.kpfu.itis.gureva.mvi.presentation.ui.screen.group.GroupScreen
import ru.kpfu.itis.gureva.mvi.presentation.ui.screen.main.MainScreen
import ru.kpfu.itis.gureva.mvi.presentation.ui.theme.MviTheme
import ru.kpfu.itis.gureva.mvi.presentation.ui.theme.bodyFontFamily
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
//                AppNavHost()
                Calendar()
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
        startDestination = Route.Home
    ) {
        composable<Route.Home> {
            MainScreen(navigateToGroup = { id ->
                navHostController.navigate(route = Route.Group(id ?: 1)) {
                    launchSingleTop = true
                }
            })
        }

        composable<Route.Group>(
//            enterTransition = { slideIntoContainer(
//                animationSpec = tween(3000, easing = EaseIn),
//                towards = AnimatedContentTransitionScope.SlideDirection.Up
//            ) }
        ) {
            GroupScreen()
        }
    }
}

