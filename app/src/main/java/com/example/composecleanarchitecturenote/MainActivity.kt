package com.example.composecleanarchitecturenote

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.composecleanarchitecturenote.feature_note.presentation.notes.components.NotesScreen
import com.example.composecleanarchitecturenote.feature_note.presentation.util.Screen
import com.example.composecleanarchitecturenote.ui.theme.ComposeCleanArchitectureNoteTheme
import com.example.composecleanarchitecturenote.feature_note.presentation.add_edit_note.components.AddEditNoteScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeCleanArchitectureNoteTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController, startDestination = Screen.NoteScreen.route
                    ) {
                        composable(route = Screen.NoteScreen.route) {
                            NotesScreen(navController = navController)
                        }
                        composable(route = Screen.AddEditNoteScreen.route + "?noteId={noteId}&noteColor={noteColor}",
                            arguments = listOf(navArgument(
                                name = "noteId"
                            ) {
                                type = NavType.IntType
                                defaultValue = -1
                            }, navArgument(
                                name = "noteColor"
                            ) {
                                type = NavType.IntType
                                defaultValue = -1
                            })) {
                            AddEditNoteScreen(
                                navController = navController,
                                noteColor = it.arguments?.getInt("noteColor") ?: -1
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ComposeCleanArchitectureNoteTheme {
        Greeting("Android")
    }
}