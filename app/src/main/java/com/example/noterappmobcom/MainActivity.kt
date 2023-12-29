package com.example.noterappmobcom

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.noterappmobcom.ui.screen.note.NoteScreen
import com.example.noterappmobcom.ui.screen.note.NoteViewModel
import com.example.noterappmobcom.ui.screen.note_list.NoteListScreen
import com.example.noterappmobcom.ui.screen.note_list.NoteListViewModel
import com.example.noterappmobcom.ui.theme.NoterAppMobcomTheme
import com.example.noterappmobcom.ui.util.Route
import com.example.noterappmobcom.ui.util.UiEvent
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NoterAppMobcomTheme{
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = Route.noteList
                ) {
                    composable(route = Route.noteList) {
                        val viewModel = hiltViewModel<NoteListViewModel>()
                        val noteList by viewModel.noteList.collectAsStateWithLifecycle()

                        NoteListScreen(
                            noteList = noteList,
                            onNoteClick = {
                                navController.navigate(
                                    Route.note.replace(
                                        "{id}",
                                        it.id.toString()
                                    )
                                )
                            },
                            onAddNoteClick = {
                                navController.navigate(Route.note)
                            }
                        )
                    }

                    composable(route = Route.note) {
                        val viewModel = hiltViewModel<NoteViewModel>()
                        val state by viewModel.state.collectAsStateWithLifecycle()

                        LaunchedEffect(key1 = true) {
                            viewModel.event.collect { event ->
                                when (event) {
                                    is UiEvent.NavigateBack -> {
                                        navController.popBackStack()
                                    }

                                    else -> Unit
                                }
                            }
                        }

                        NoteScreen(
                            state = state,
                            onEvent = viewModel::onEvent
                        )
                    }
                }
            }
        }
    }

}