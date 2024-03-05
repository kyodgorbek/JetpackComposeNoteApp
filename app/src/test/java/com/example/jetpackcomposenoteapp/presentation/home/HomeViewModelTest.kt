package com.example.jetpackcomposenoteapp.presentation.home


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.viewModelScope
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.jetpackcomposenoteapp.common.ScreenViewState
import com.example.jetpackcomposenoteapp.data.local.model.Note
import com.example.jetpackcomposenoteapp.domain.use_cases.DeleteNoteUseCase
import com.example.jetpackcomposenoteapp.domain.use_cases.GetAllNotesUseCase
import com.example.jetpackcomposenoteapp.domain.use_cases.UpdateNoteUseCase
import com.example.jetpackcomposenoteapp.presentation.CoroutinesTestExtension
import com.example.jetpackcomposenoteapp.presentation.InstantTaskExecutorExtension
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.runner.RunWith
import java.util.Date

@RunWith(AndroidJUnit4::class)
@ExtendWith(InstantTaskExecutorExtension::class, CoroutinesTestExtension::class)
class HomeViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @MockK
    private lateinit var deleteNoteUseCase: DeleteNoteUseCase

    @MockK
    private lateinit var updateNoteUseCase: UpdateNoteUseCase

    @MockK
    private lateinit var getAllNotesUseCase: GetAllNotesUseCase // Add GetAllNotesUseCase

    private lateinit var viewModel: HomeViewModel
    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)

        // No need to create a mock ViewModel with relaxed mode. Use a directly initialized instance.
        viewModel = HomeViewModel(
            deleteNoteUseCase = deleteNoteUseCase,
            updateNoteUseCase = updateNoteUseCase,
            getAllNotesUseCase = getAllNotesUseCase // Inject all required dependencies
        )

        // Configure mock behavior for dependencies (if applicable)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `getAllNotes should update state with list of notes on success`() =
        testDispatcher.runBlockingTest {

            val expectedNotes = listOf(Note(1, "Note 1", "Content 1", Date(), false))
            val getAllNotesUseCase = mockk<GetAllNotesUseCase>()
            coEvery { getAllNotesUseCase() }.returns(flowOf(expectedNotes))
            var deleteNoteUseCase = mockk<DeleteNoteUseCase>()

            var updateNoteUseCase = mockk<UpdateNoteUseCase>()
            var viewModel = HomeViewModel(getAllNotesUseCase, deleteNoteUseCase, updateNoteUseCase)


            viewModel.viewModelScope.launch { } // Trigger coroutine launch

            val actualState = viewModel.state.first()

            assert(actualState.notes is ScreenViewState.Success)
            val actualNotes = (actualState.notes as ScreenViewState.Success).data
            assertEquals(expectedNotes, actualNotes)
        }


    // ... other tests ...


    @Test
    fun `deleteNote should call deleteNoteUseCase with correct noteId`() = runBlockingTest {
        // Mock dependencies
        val getAllNotesUseCase = mockk<GetAllNotesUseCase>()
        val deleteNoteUseCase = mockk<DeleteNoteUseCase>()
        val updateNoteUseCase = mockk<UpdateNoteUseCase>()

        // Define the response for getAllNotesUseCase
        val testNotes = listOf(
            Note(
                id = 1L,
                title = "My Note Title",
                content = "My Note Content",
                createdDate = Date(),
                isBookMarked = false
            )
        )
        coEvery { getAllNotesUseCase.invoke() } returns flowOf(testNotes)

        // Initialize view model after setting up the mock
        val viewModel = HomeViewModel(getAllNotesUseCase, deleteNoteUseCase, updateNoteUseCase)

        // Define test data
        val noteId = 1L

        // Call the function being tested
        viewModel.deleteNote(noteId)

        // Verify that deleteNoteUseCase is called with the correct noteId
        coVerify { deleteNoteUseCase(noteId) }
    }

    @Test
    fun `onBookMarkChange should call updateNoteUseCase with updated note`() = runBlockingTest {
        // Mock dependencies
        val getAllNotesUseCase = mockk<GetAllNotesUseCase>()
        val deleteNoteUseCase = mockk<DeleteNoteUseCase>()
        val updateNoteUseCase = mockk<UpdateNoteUseCase>()

        // Define a mock response for GetAllNotesUseCase
        val testNotes = listOf(
            Note(
                id = 1L,
                title = "Note",
                content = "Content",
                createdDate = Date(),
                isBookMarked = false
            )
        )
        coEvery { getAllNotesUseCase.invoke() } returns flowOf(testNotes)

        // Initialize view model with mocked dependencies
        val viewModel = HomeViewModel(getAllNotesUseCase, deleteNoteUseCase, updateNoteUseCase)

        // Define the note to test
        val note = Note(
            id = 1L,
            title = "Note",
            content = "Content",
            createdDate = Date(),
            isBookMarked = false
        )

        // Call the function being tested
        viewModel.onBookMarkChange(note)

        // Verify that updateNoteUseCase is called with the updated note
        coVerify { updateNoteUseCase(note.copy(isBookMarked = true)) }
    }
}

