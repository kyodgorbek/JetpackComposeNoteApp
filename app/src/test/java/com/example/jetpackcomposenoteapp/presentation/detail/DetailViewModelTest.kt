package com.example.jetpackcomposenoteapp.presentation.detail

import androidx.lifecycle.viewModelScope
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.jetpackcomposenoteapp.data.local.model.Note
import com.example.jetpackcomposenoteapp.domain.use_cases.AddUseCase
import com.example.jetpackcomposenoteapp.domain.use_cases.GetNoteByIdUseCase
import com.example.jetpackcomposenoteapp.presentation.CoroutinesTestExtension
import com.example.jetpackcomposenoteapp.presentation.InstantTaskExecutorExtension
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.runner.RunWith
import java.util.Date

@RunWith(AndroidJUnit4::class)
@ExtendWith(InstantTaskExecutorExtension::class, CoroutinesTestExtension::class)
class DetailViewModelTest {

    @MockK
    private lateinit var addUseCase: AddUseCase

    @MockK
    private lateinit var getNoteByIdUseCase: GetNoteByIdUseCase
    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `initialize should set isUpdatingNote based on noteId`() = testDispatcher.runBlockingTest {
        val noteId1 = 1L
        val noteId2 = -1L
        val detailViewModel1 = DetailViewModel(mockk(), mockk(), noteId1)
        val detailViewModel2 = DetailViewModel(mockk(), mockk(), noteId2)

        detailViewModel1.initialize()
        detailViewModel2.initialize()

        assertTrue(detailViewModel1.state.isUpdatingNote)
        assertFalse(detailViewModel2.state.isUpdatingNote)
    }

    @Test
    fun `getNoteById should update state with the fetched note`() = testDispatcher.runBlockingTest {
        val noteId = 1L
        val expectedTitle = "My Note Title"
        val expectedContent = "My Note Content"

        val mockGetNoteByIdUseCase = mockk<GetNoteByIdUseCase>()
        coEvery { mockGetNoteByIdUseCase(noteId) }.returns(
            flowOf(
                Note(
                    id = noteId,
                    title = expectedTitle,
                    content = expectedContent,
                    createdDate = Date(),
                    isBookMarked = false
                )
            )
        )

        val detailViewModel = DetailViewModel(mockk(), mockGetNoteByIdUseCase, noteId)

        detailViewModel.viewModelScope.launch { detailViewModel.getNoteById() }
        val actualState = detailViewModel.state

        assertEquals(expectedTitle, actualState.title)
        assertEquals(expectedContent, actualState.content)
    }

    @Test
    fun `onContentChange should update the state's content`() = testDispatcher.runBlockingTest {
        val detailViewModel = DetailViewModel(mockk(), mockk(), -1L)
        val newContent = "Updated content"

        detailViewModel.onContentChange(newContent)

        assertEquals(newContent, detailViewModel.state.content)
    }

    @Test
    fun `onTitleChange should update the state's title`() = testDispatcher.runBlockingTest {
        val detailViewModel = DetailViewModel(mockk(), mockk(), -1L)
        val newTitle = "Updated title"

        detailViewModel.onTitleChange(newTitle)

        assertEquals(newTitle, detailViewModel.state.title)
    }

    @Test
    fun `onBookMarkChange should update the state's isBookmark`() = testDispatcher.runBlockingTest {
        val detailViewModel = DetailViewModel(mockk(), mockk(), -1L)

        detailViewModel.onBookMarkChange(true)

        assertTrue(detailViewModel.state.isBookmark)
    }

    @Test
    fun `isFormNotBlank should return true when title and content are not empty`() =
        testDispatcher.runBlockingTest {
            val detailViewModel = DetailViewModel(mockk(), mockk(), -1L)
            detailViewModel.state = detailViewModel.state.copy(title = "Title", content = "Content")

            val isNotBlank = detailViewModel.isFormNotBlank

            assertTrue(isNotBlank)
        }

    @Test
    fun `isFormNotBlank should return false when title is empty`() =
        testDispatcher.runBlockingTest {
            val detailViewModel = DetailViewModel(mockk(), mockk(), -1L)
            detailViewModel.state = detailViewModel.state.copy(content = "Content")

            val isNotBlank = detailViewModel.isFormNotBlank

            assertFalse(isNotBlank)
        }

    @Test
    fun `isFormNotBlank should return false when content is empty`() =
        testDispatcher.runBlockingTest {
            val detailViewModel = DetailViewModel(mockk(), mockk(), -1L)
            detailViewModel.state = detailViewModel.state.copy(title = "Title")

            val isNotBlank = detailViewModel.isFormNotBlank

            assertFalse(isNotBlank)
        }
}