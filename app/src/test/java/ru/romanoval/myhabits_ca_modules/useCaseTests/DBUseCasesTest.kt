package ru.romanoval.myhabits_ca_modules.useCaseTests

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import ru.romanoval.domain.mapper.CloudErrorMapper
import ru.romanoval.domain.model.Habit
import ru.romanoval.domain.repositories.AppRepository
import ru.romanoval.domain.useCases.db.*
import ru.romanoval.myhabits_ca_modules.MainCoroutineScopeRule

@RunWith(JUnit4::class)
class DBUseCasesTest {

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

        getHabitsDBUseCase = GetHabitsDBUseCase(cloudErrorMapper, appRepository)
        getBadHabitsDBUseCase = GetBadHabitsDBUseCase(cloudErrorMapper, appRepository)
        getGoodHabitsDBUseCase = GetGoodHabitsDBUseCase(cloudErrorMapper, appRepository)
        deleteHabitsDBUseCase = DeleteHabitDBUseCase(cloudErrorMapper, appRepository)
        deleteAllHabitsDBUseCase = DeleteAllHabitsDBUseCase(cloudErrorMapper, appRepository)
        insertHabitDBUseCase = InsertHabitDBUseCase(cloudErrorMapper, appRepository)
        insertHabitsDBUseCase = InsertHabitsDBUseCase(cloudErrorMapper, appRepository)
        updateHabitDBUseCase = UpdateHabitDBUseCase(cloudErrorMapper, appRepository)

    }

    @Mock
    lateinit var mockedFlowAllHabits: Flow<List<Habit>>
    private lateinit var getHabitsDBUseCase: GetHabitsDBUseCase

    @Test
    fun `GetHabitsDbUseCase test`() {
        `when`(appRepository.selectAllHabits()).thenReturn(mockedFlowAllHabits)

        assertEquals(mockedFlowAllHabits, getHabitsDBUseCase.getHabits())
    }


    @Mock
    lateinit var mockedFlowBadHabits: Flow<List<Habit>>
    private lateinit var getBadHabitsDBUseCase: GetBadHabitsDBUseCase

    @Test
    fun `GetBadHabitsUseCase test`() {
        `when`(appRepository.selectBadHabitsFromDb()).thenReturn(mockedFlowBadHabits)

        assertEquals(mockedFlowBadHabits, getBadHabitsDBUseCase.getBadHabits())
    }


    @Mock
    private lateinit var mockedFlowGoodHabits: Flow<List<Habit>>
    private lateinit var getGoodHabitsDBUseCase: GetGoodHabitsDBUseCase

    @Test
    fun `GetGoodHabitsUseCase test`() {
        `when`(appRepository.selectGoodHabitsFromDb()).thenReturn(mockedFlowGoodHabits)

        assertEquals(mockedFlowGoodHabits, getGoodHabitsDBUseCase.getGoodHabits())
    }


    @Mock
    lateinit var habitToDelete: Habit
    @Mock
    lateinit var mockedHabit: Habit
    @Mock
    lateinit var deleteHabitsDBUseCase: DeleteHabitDBUseCase

    @Test
    fun `DeleteHabitUseCase test`() {

        val mockedHabitsInDb: MutableList<Habit> =
            mutableListOf(habitToDelete, mockedHabit, mockedHabit)


        runBlocking {
            `when`(appRepository.deleteHabit(habitToDelete)).thenAnswer {
                mockedHabitsInDb.remove(
                    habitToDelete
                )
            }
            deleteHabitsDBUseCase.habit = habitToDelete
            deleteHabitsDBUseCase.execute {}
            delay(1000)
        }

        assertEquals(2, mockedHabitsInDb.size)
    }

    private lateinit var deleteAllHabitsDBUseCase: DeleteAllHabitsDBUseCase

    @Test
    fun `DeleteAllHabitsDBUseCase test`(){

        val mockedHabitsInDb: MutableList<Habit> =
            mutableListOf(habitToDelete, habitToDelete, habitToDelete)


        runBlocking {
            `when`(appRepository.deleteAllHabitsFromDb()).thenAnswer {
                mockedHabitsInDb.clear()
            }
            deleteAllHabitsDBUseCase.execute {}
            delay(1000)
        }
        assertEquals(0, mockedHabitsInDb.size)
    }

    private lateinit var insertHabitDBUseCase: InsertHabitDBUseCase

    @Test
    fun `InsertHabitDBUseCase test`(){

        val mockedHabitsInDb: MutableList<Habit> = mutableListOf()

        runBlocking {
            `when`(appRepository.insertHabit(mockedHabit)).thenAnswer { mockedHabitsInDb.add(mockedHabit) }

            insertHabitDBUseCase.habit = mockedHabit
            insertHabitDBUseCase.execute {}
            delay(1000)
            insertHabitDBUseCase.execute{}
            delay(1000)
        }
        assertEquals(2, mockedHabitsInDb.size)
    }


    private lateinit var insertHabitsDBUseCase: InsertHabitsDBUseCase

    @Test
    fun `InsertHabitsDBUseCase test`(){

        val mockedHabitsInDb: MutableList<Habit> = mutableListOf()

        val mockedHabitsToInsert = listOf(mockedHabit, mockedHabit, mockedHabit, mockedHabit, mockedHabit) //5

        runBlocking {
            `when`(appRepository.insertHabits(mockedHabitsToInsert)).thenAnswer { mockedHabitsInDb.addAll(mockedHabitsToInsert) }

            insertHabitsDBUseCase.habits = mockedHabitsToInsert
            insertHabitsDBUseCase.execute {}
            delay(1000)
        }
        assertEquals(5, mockedHabitsInDb.size)
    }

    private lateinit var updateHabitDBUseCase: UpdateHabitDBUseCase

    @Mock
    lateinit var mockedUpdatedHabit: Habit

    @Test
    fun `updateHabitUseCase test`(){
        val mockedHabitsInDb: MutableList<Habit> = mutableListOf(mockedHabit)


        runBlocking{
            `when`(appRepository.updateHabit(mockedHabit)).thenAnswer {
                mockedHabitsInDb.add(mockedUpdatedHabit)
                mockedHabitsInDb.remove(mockedHabit)
            }

            updateHabitDBUseCase.habit = mockedHabit
            updateHabitDBUseCase.execute {}
            delay(1000)
        }
        assertEquals(mockedUpdatedHabit, mockedHabitsInDb[0])
    }

}