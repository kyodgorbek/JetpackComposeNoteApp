package com.example.jetpackcomposenoteapp.presentation.bookmark

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.jetpackcomposenoteapp.common.ScreenViewState
import com.example.jetpackcomposenoteapp.common.util.TestTag
import com.example.jetpackcomposenoteapp.data.local.model.Note
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Date

@RunWith(AndroidJUnit4::class)
class BookmarkScreenTest {

    @Rule
    @JvmField
    val composeTestRule = createComposeRule()

    private val testNote1 = Note(
        id = 1,
        title = "Test Note 1",
        content = "This is a test note 1",
        createdDate = Date(),
        isBookMarked = true
    )
    private val testNote2 = Note(
        id = 2,
        title = "Test Note 2",
        content = "This is a test note 2",
        createdDate = Date(),
        isBookMarked = true
    )
    private val notes = listOf(testNote1, testNote2)

    @Test
    fun test_bookmarkscreenshowsnoteswhensuccess() {
        composeTestRule.setContent {
            BookmarkScreen(
                state = BookmarkState(ScreenViewState.Success(notes)),
                onBookMarkChange = {},
                onDelete = {},
                onNoteClicked = {}
            )
        }


        // Verify LazyColumn content
        composeTestRule.onNodeWithText(testNote1.title).assertIsDisplayed()
        composeTestRule.onNodeWithText(testNote2.title).assertIsDisplayed()
    }

    @Test
    fun testBookmarkScreen_showsloadingindicatorwhenloading() {
        composeTestRule.setContent {
            BookmarkScreen(
                state = BookmarkState(ScreenViewState.Loading),
                onBookMarkChange = {},
                onDelete = {},
                onNoteClicked = {}
            )
        }

        // Verify CircularProgressIndicator presence
        composeTestRule.onNodeWithTag(TestTag.LOADING_INDICATOR).assertIsDisplayed()
    }

    @Test
    fun testBookmark_ScreenShowsNotesWhenSuccess() {
        composeTestRule.setContent {
            BookmarkScreen(
                state = BookmarkState(ScreenViewState.Error("BookMark Error")),
                onBookMarkChange = {},
                onDelete = {},
                onNoteClicked = {}
            )
        }

        // Verify error text is displayed
        composeTestRule.onNodeWithText("BookMark Error").assertIsDisplayed()
    }



}