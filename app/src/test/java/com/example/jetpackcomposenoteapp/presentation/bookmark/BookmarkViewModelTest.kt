package com.example.jetpackcomposenoteapp.presentation.bookmark

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.jetpackcomposenoteapp.common.ScreenViewState
import com.example.jetpackcomposenoteapp.data.local.model.Note
import com.example.jetpackcomposenoteapp.domain.use_cases.DeleteNoteUseCase
import com.example.jetpackcomposenoteapp.domain.use_cases.FilteredBookmarkNotes
import com.example.jetpackcomposenoteapp.domain.use_cases.UpdateNoteUseCase
import com.example.jetpackcomposenoteapp.presentation.CoroutinesTestExtension
import com.example.jetpackcomposenoteapp.presentation.InstantTaskExecutorExtension
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@ExtendWith(InstantTaskExecutorExtension::class, CoroutinesTestExtension::class)
class BookmarkViewModelTest {

    @MockK
    lateinit var updateNoteUseCase: UpdateNoteUseCase

    @MockK
    lateinit var filteredBookmarkNotes: FilteredBookmarkNotes

    @MockK
    lateinit var note: Note
    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setUp() {
        mockkStatic(filteredBookmarkNotes::class)
        Dispatchers.setMain(testDispatcher)
        // Mock static method
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `getBookmarkedNotes should call filteredBookmarkNotes and update state`() =
        testDispatcher.runBlockingTest {

            val filteredBookmarkNotes = mockk<FilteredBookmarkNotes>()
            val note = mockk<Note>()
            val updateNoteUseCase = mockk<UpdateNoteUseCase>()

            // Mock behavior
            every { filteredBookmarkNotes() } returns flowOf(listOf(note))

            // Create and inject mocks
            val viewModel = BookmarkViewModel(
                updateNoteUseCase,
                filteredBookmarkNotes,
                mockk()  // Mock or use actual implementation for deleteNoteUseCase
            )

            // Trigger the function
            viewModel.getBookMarkedNotes()

            // Verify state update
            val expectedState = BookmarkState(notes = ScreenViewState.Success(listOf(note)))
            assertThat(viewModel.state.first(), equalTo(expectedState))

            // Verify interaction with filteredBookmarkNotes
            verify { filteredBookmarkNotes() } // Verify static method call
        }

    @Test
    fun `onBookmarkChange should call updateNoteUseCase with updated note`() =
        testDispatcher.runBlockingTest {

            // Mock dependencies
            val mockNote = mockk<Note>()
            val mockUpdateNoteUseCase = mockk<UpdateNoteUseCase>()
            val mockFilteredBookmarkNotes = mockk<FilteredBookmarkNotes>()

            // Stub the behavior of mockNote
            every { mockNote.isBookMarked } returns false
            every { mockNote.copy(isBookMarked = any()) } returns mockNote

            // Stub the behavior of FilteredBookmarkNotes
            val mockFilteredFlow = flowOf(listOf(mockNote)) // Mocked filtered list of notes
            coEvery { mockFilteredBookmarkNotes.invoke() } returns mockFilteredFlow

            // Create an instance of BookmarkViewModel with mocked dependencies
            val viewModel =
                BookmarkViewModel(mockUpdateNoteUseCase, mockFilteredBookmarkNotes, mockk())

            // Trigger the function
            viewModel.onBookmarkChange(mockNote)

            // Verify interaction with updateNoteUseCase
            val expectedNote = mockNote.copy(isBookMarked = true)
            coVerify { mockUpdateNoteUseCase(expectedNote) }
        }

    @Test
    fun `deleteNote should call deleteNoteUseCase with correct id`() =
        testDispatcher.runBlockingTest {

            val noteId = 1L

            val deleteNoteUseCase = mockk<DeleteNoteUseCase>()
            val filteredBookmarkNotes = mockk<FilteredBookmarkNotes>()

            // Stub the behavior of FilteredBookmarkNotes
            val mockFilteredFlow = flowOf(listOf(mockk<Note>())) // Mocked filtered list of notes
            every { filteredBookmarkNotes.invoke() } returns mockFilteredFlow

            // Create and inject mocks
            val viewModel = BookmarkViewModel(
                mockk(), // Mock or use actual implementation for updateNoteUseCase
                filteredBookmarkNotes,
                deleteNoteUseCase
            )

            // Trigger the function
            viewModel.deleteNote(noteId)

            // Verify interaction with deleteNoteUseCase
            coVerify { deleteNoteUseCase(noteId) }
        }
}