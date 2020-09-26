package ru.romanoval.myhabits_ca_modules

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.junit.Assert.*
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import ru.romanoval.domain.model.Habit
import ru.romanoval.myhabits_ca_modules.core.util.observeOnce
import ru.romanoval.myhabits_ca_modules.ui.viewModels.GoodHabitsViewModel

@RunWith(JUnit4::class)
class ViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val coroutineScope = MainCoroutineScopeRule()

    @Mock
    lateinit var habits: List<Habit>

    @Mock
    lateinit var sortedHabits: List<Habit>

    @Mock
    private lateinit var goodHabitsViewModel: GoodHabitsViewModel




    @Before
    fun setUp(){
        MockitoAnnotations.initMocks(this)

    }

    @ExperimentalCoroutinesApi
    @Test
    fun `message LiveData test`(){

        runBlocking {
            val messageLiveData = flow{
                emit("test")
            }.asLiveData()

            doReturn(messageLiveData).`when`(goodHabitsViewModel).message

            goodHabitsViewModel.message.observeOnce {
                assertEquals(it, "test")
            }
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `messageWithUndo LiveData test`(){
        runBlocking {
            val messageWithUndoLiveData = flow{
                emit("testUndo")
            }.asLiveData()

            doReturn(messageWithUndoLiveData).`when`(goodHabitsViewModel).messageWithUndo

            goodHabitsViewModel.messageWithUndo.observeOnce {
                assertEquals(it, "testUndo")
            }
        }

    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Habits LiveData test`(){
        runBlocking {
            val habitsLiveData = flow{
                emit(habits)
            }.asLiveData()

            doReturn(habitsLiveData).`when`(goodHabitsViewModel).habits

            val liveDataTest = goodHabitsViewModel.habits

            liveDataTest?.observeOnce {
                assertEquals(it,habits)
            }

        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `sortedHabits LiveData test`(){
        runBlocking {
            val sortedHabitsLiveData = flow{
                emit(sortedHabits)
            }.asLiveData()

            doReturn(sortedHabitsLiveData).`when`(goodHabitsViewModel).sortedHabits

            val liveDataTest = goodHabitsViewModel.sortedHabits

            liveDataTest?.observeOnce {
                assertEquals(it, sortedHabits)
            }
        }
    }


}