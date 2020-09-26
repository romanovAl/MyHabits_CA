package ru.romanoval.myhabits_ca_modules.useCaseTests

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Assert.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import ru.romanoval.domain.model.Habit
import ru.romanoval.domain.repositories.AppRepository
import ru.romanoval.domain.useCases.sort.GetSortedHabitsUseCase
import ru.romanoval.domain.useCases.sort.SetSortedHabitsUseCase
import ru.romanoval.myhabits_ca_modules.MainCoroutineScopeRule

@RunWith(JUnit4::class)
class SortedHabitsUseCasesTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val coroutineScope =
        MainCoroutineScopeRule()

    @Mock
    lateinit var appRepository: AppRepository
    @Mock
    lateinit var mockedHabit: Habit

    @Before
    fun setUp(){
        MockitoAnnotations.initMocks(this)

        getSortedHabitsUseCase = GetSortedHabitsUseCase(appRepository)
        setSortedHabitsUseCase = SetSortedHabitsUseCase(appRepository)
    }

    @Mock
    private lateinit var mockedFlowSortedHabits : Flow<List<Habit>>
    private lateinit var getSortedHabitsUseCase : GetSortedHabitsUseCase

    @Test
    fun `getSortedHabitsUseCase test`(){


        `when`(appRepository.getSortedHabits()).thenReturn(mockedFlowSortedHabits)

        assertEquals(mockedFlowSortedHabits,getSortedHabitsUseCase.getSortedHabits())

    }

    private lateinit var setSortedHabitsUseCase: SetSortedHabitsUseCase
    @Test
    fun `setSortedHabitsUseCase test`(){

        val sortedHabits = mutableListOf<Habit>()

        val habitsToSet = listOf(mockedHabit,mockedHabit,mockedHabit)

        `when`(appRepository.setSortedHabits(habitsToSet)).thenAnswer {
            sortedHabits.addAll(habitsToSet)
        }

        setSortedHabitsUseCase.setSortedHabits(habitsToSet)

        assertEquals(3, sortedHabits.size)


    }
}