package com.example.composecleanarchitecturenote.feature_note.presentation.notes.components

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.composecleanarchitecturenote.feature_note.presentation.notes.NotesEvent
import com.example.composecleanarchitecturenote.feature_note.presentation.notes.NotesViewModel
import com.example.composecleanarchitecturenote.feature_note.presentation.util.Screen
import kotlinx.coroutines.launch


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun NotesScreen(
    navController: NavController, viewModel: NotesViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    //val scaffoldState = rememberScaffoldState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(floatingActionButton = {
        FloatingActionButton(
            onClick = {
                navController.navigate(Screen.AddEditNoteScreen.route)

            }, containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add Note")
        }
    }, snackbarHost = { SnackbarHost(snackbarHostState) }) { padding->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Your Note", style = MaterialTheme.typography.bodyMedium
                )
                IconButton(onClick = { viewModel.onEvent(NotesEvent.ToggleOrderSection) }) {
                    Icon(imageVector = Icons.Default.Sort, contentDescription = "Sort")
                }
            }
            AnimatedVisibility(
                visible = state.isOrderSectionVisible,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                OrderSection(modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                    noteOrder = state.noteOrder,
                    onOrderChange = {
                        viewModel.onEvent(NotesEvent.Order(it))
                    })
            }
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(state.notes) { note ->
                    NoteItem(note = note, modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate(
                                Screen.AddEditNoteScreen.route + "?noteId=${note.id}&noteColor=${note.color}"
                            )
                        }, onDeleteClick = {
                        viewModel.onEvent(NotesEvent.DeleteNote(note))
                        scope.launch {
                            val result = snackbarHostState.showSnackbar(
                                "Note Deleted", "Undo"
                            )
                            if (result == SnackbarResult.ActionPerformed) {
                                viewModel.onEvent(NotesEvent.RestoreNote)
                            }

                        }
                    })
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }


}