package ru.romanoval.myhabits_ca_modules.useCaseTests

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import ru.romanoval.domain.mapper.CloudErrorMapper
import ru.romanoval.domain.model.Habit
import ru.romanoval.domain.repositories.AppRepository
import ru.romanoval.domain.useCases.api.DeleteHabitsApiUseCase
import ru.romanoval.domain.useCases.api.GetHabitsApiUseCase
import ru.romanoval.domain.useCases.api.PostHabitsDoneApiUseCase
import ru.romanoval.domain.useCases.api.PutHabitsApiUseCase
import ru.romanoval.myhabits_ca_modules.MainCoroutineScopeRule

@RunWith(JUnit4::class)
class ApiUseCaseTests {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val coroutineScope =
        MainCoroutineScopeRule()

    @Mock
    lateinit var appRepository: AppRepository

    @Mock
    lateinit var cloudErrorMapper: CloudErrorMapper

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        getHabitsApiUseCase = GetHabitsApiUseCase(cloudErrorMapper, appRepository)
        deleteHabitsApiUseCase = DeleteHabitsApiUseCase(cloudErrorMapper, appRepository)
        postHabitsDoneApiUseCase = PostHabitsDoneApiUseCase(cloudErrorMapper, appRepository)
        putHabitsApiUseCase = PutHabitsApiUseCase(cloudErrorMapper, appRepository)
    }

    @Mock
    private lateinit var mockedHabit: Habit
    private lateinit var getHabitsApiUseCase: GetHabitsApiUseCase

    @Test
    fun `GetHabitsApiUseCase test`() {

        val mockedHabitsInApi = mutableListOf(mockedHabit, mockedHabit, mockedHabit)

        var habitsFromApi: List<Habit> = listOf()

        runBlocking {
            `when`(appRepository.getHabitsFromApi()).thenReturn(mockedHabitsInApi)
            getHabitsApiUseCase.execute {
                onComplete {
                    habitsFromApi = it
                }
            }
            delay(1000)
        }

        assertEquals(3, habitsFromApi.size)
    }

    @Mock
    private lateinit var habitToDelete: Habit

    private lateinit var deleteHabitsApiUseCase : DeleteHabitsApiUseCase
    @Test
    fun `DeleteHabitsApiUseCase test`() {

        val mockedHabitsInApi = mutableListOf(habitToDelete, mockedHabit, mockedHabit)


        val habitsToDelete = mutableListOf(habitToDelete, mockedHabit)

        runBlocking {

            `when`(appRepository.deleteHabitsInApi(habitsToDelete)).thenAnswer {
                habitsToDelete.forEach {
                    mockedHabitsInApi.remove(it)
                }
            }

            deleteHabitsApiUseCase.habits = habitsToDelete
            deleteHabitsApiUseCase.execute {}
            delay(1000)
        }

        assertEquals(1, mockedHabitsInApi.size)

    }

    private lateinit var postHabitsDoneApiUseCase: PostHabitsDoneApiUseCase

    @Test
    fun `PostHabitsDoneApiUseCase test`(){

        val habitWithoutHabitsDone = Habit("",123,"","",1,1,1,1,1,123, mutableListOf())


        val habitWithHabitsDone = Habit("",123,"","",1,1,1,1,1,123,
            mutableListOf(123, 124, 125))

        val mockedHabitsInApi = mutableListOf(habitWithoutHabitsDone, habitWithoutHabitsDone, habitWithoutHabitsDone)
        val habitsWithoutHabitsDone = mutableListOf(habitWithoutHabitsDone, habitWithoutHabitsDone, habitWithoutHabitsDone)
        val habitsWithHabitsDone = mutableListOf(habitWithHabitsDone,habitWithHabitsDone,habitWithHabitsDone)

        runBlocking {
            `when`(appRepository.postHabitsDoneInApi(habitsWithoutHabitsDone,habitsWithHabitsDone)).thenAnswer {

                mockedHabitsInApi.clear()
                mockedHabitsInApi.addAll(habitsWithHabitsDone)

            }

            postHabitsDoneApiUseCase.habitsFromApi = habitsWithoutHabitsDone
            postHabitsDoneApiUseCase.habitsFromDb = habitsWithHabitsDone
            postHabitsDoneApiUseCase.execute {}
            delay(1000)

        }

        mockedHabitsInApi.forEach {
            assertEquals(3, it.done_dates.size)
        }
    }

    @Mock
    private lateinit var habitToPut: Habit

    private lateinit var putHabitsApiUseCase: PutHabitsApiUseCase
    @Test
    fun `PutHabitsApiUseCase test`(){

        val mockedHabitsInApi = mutableListOf(mockedHabit, mockedHabit)

        val habitsToPutInApi = mutableListOf(habitToPut, habitToPut, habitToPut)

        runBlocking {
            `when`(appRepository.putHabitsInApi(habitsToPutInApi)).thenAnswer {
                mockedHabitsInApi.addAll(habitsToPutInApi)
            }

            putHabitsApiUseCase.habits = habitsToPutInApi
            putHabitsApiUseCase.execute {  }
            delay(1000)
        }

        assertEquals(5, mockedHabitsInApi.size)

    }



}