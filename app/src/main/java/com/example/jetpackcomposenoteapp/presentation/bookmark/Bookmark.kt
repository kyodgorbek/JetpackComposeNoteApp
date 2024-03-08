package com.example.jetpackcomposenoteapp.presentation.bookmark

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jetpackcomposenoteapp.common.ScreenViewState
import com.example.jetpackcomposenoteapp.data.local.model.Note
import com.example.jetpackcomposenoteapp.presentation.home.NoteCard
import com.example.jetpackcomposenoteapp.presentation.home.notes
import com.example.jetpackcomposenoteapp.common.util.TestTag

@Composable
fun BookmarkScreen(
    state: BookmarkState,
    modifier: Modifier = Modifier,
    onBookMarkChange: (note: Note) -> Unit,
    onDelete: (Long) -> Unit,
    onNoteClicked: (Long) -> Unit

) {
    when (state.notes) {
        is ScreenViewState.Loading -> {
            CircularProgressIndicator(modifier = Modifier.testTag(TestTag.LOADING_INDICATOR))
        }

        is ScreenViewState.Success -> {
            val notes = state.notes.data
            LazyColumn(contentPadding = PaddingValues(4.dp), modifier = modifier) {
                itemsIndexed(notes) { index: Int, note: Note ->
                    NoteCard(
                        index = index,
                        note = note,
                        onBookmarkChange = onBookMarkChange,
                        onDeleteNote = onDelete,
                        onNoteClicked = onNoteClicked
                    )
                }
            }
        }

        is ScreenViewState.Error -> {
            Text(
                text = state.notes.message ?: "Uknown Error",
                color = MaterialTheme.colorScheme.error
            )

        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PrevBookMark() {
    BookmarkScreen(
        state = BookmarkState(
            notes = ScreenViewState.Success(notes)
        ),

        onBookMarkChange = {},
        onDelete = {},
        onNoteClicked = {}
    )
}