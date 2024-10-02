package ru.kpfu.itis.gureva.mvi.presentation.ui.screen.main

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.kpfu.itis.gureva.mvi.R
import ru.kpfu.itis.gureva.mvi.presentation.ui.noRippleClickable
import ru.kpfu.itis.gureva.mvi.presentation.ui.theme.bodyFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainBottomSheet(viewModel: MainViewModel = hiltViewModel()) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    val state by viewModel.state.collectAsStateWithLifecycle()

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = { viewModel.obtainEvent(MainScreenEvent.OnBottomSheetClose) },
        shape = RoundedCornerShape(8.dp),
        dragHandle = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Text(
                    text = stringResource(R.string.new_list),
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.align(Alignment.Center)
                )

                Icon(
                    Icons.Filled.Clear,
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .noRippleClickable {
                            scope
                                .launch { sheetState.hide() }
                                .invokeOnCompletion {
                                    if (!sheetState.isVisible) {
                                        viewModel.obtainEvent(MainScreenEvent.OnBottomSheetClose)
                                    }
                                }
                        }
                )
            }
        }
    ) {
        var nameList by remember {
            mutableStateOf("")
        }
        val focusRequester = remember { FocusRequester() }

        BasicTextField(
            value = nameList,
            onValueChange = { nameList = it },
            modifier = Modifier
                .padding(start = 24.dp, end = 24.dp, bottom = 24.dp)
                .focusRequester(focusRequester),
            textStyle = TextStyle(fontFamily = bodyFontFamily, fontSize = 18.sp, lineHeight = 28.sp),
            minLines = 5,
            maxLines = 5
        )

        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }

        //чтобы вместо плюса на секунду появлялся текст и исчезал
//        AnimatedVisibility(visible = state.groupNameError) {
//            Text("error")
//        }

        Column {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.secondary
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 56.dp)
                        .noRippleClickable {
                            viewModel.obtainEvent(MainScreenEvent.OnGroupSaveClicked(nameList))
                        },
                ) {
                    val action by viewModel.action.collectAsStateWithLifecycle()
                    var visibility by remember {
                        mutableStateOf(false)
                    }

                    var first by remember {
                        mutableStateOf(true)
                    }
                    LaunchedEffect(key1 = action) {
                        if (!first) visibility = true
                        else first = false
                    }

//                    if (anim == 0) {
//                        Text(
//                            text = "error",
//                            modifier = Modifier.align(Alignment.Center)
//                        )
//                        Log.e("", "true")
//                    }
//                    else {
//                        Icon(
//                            Icons.Filled.Add,
//                            contentDescription = null,
//                            modifier = Modifier.align(Alignment.Center)
//                        )
//                        Log.e("", "false")
//                    }

//
                    AnimatedContent(
                        targetState = visibility,
                        label = ""
                    ) { error ->
                        if (error) {
                            Text(
                                text = "error",
                                modifier = Modifier.align(Alignment.Center)
                            )

                            LaunchedEffect(true) {
                                delay(1000)
                                visibility = false
                            }
                        }
                        else {
                            Icon(
                                Icons.Filled.Add,
                                contentDescription = null,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
//
//                    if (state.groupNameError) {
//                        Log.e("", "true")
////                        visibility = true
//                    }
//                    else {
//                        Log.e("", "false")
//                    }
                }
            }
        }
    }
}
