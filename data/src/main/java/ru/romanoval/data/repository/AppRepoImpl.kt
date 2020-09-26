package ru.romanoval.data.repository

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.romanoval.data.mapper.HabitMapper
import ru.romanoval.data.source.cloud.BaseCloudRepository
import ru.romanoval.data.source.db.HabitsDao
import ru.romanoval.data.source.tempSort.SortedHabits
import ru.romanoval.domain.model.Habit
import ru.romanoval.domain.repositories.AppRepository
import javax.inject.Inject

class AppRepoImpl @Inject constructor(
    private val cloudRepository: BaseCloudRepository,
    private val habitsDao: HabitsDao,
    private val sortedHabits: SortedHabits
) : AppRepository {

    private var habitMapper = HabitMapper()

    //--------------DATABASE--------------------
    override fun selectAllHabits(): Flow<List<Habit>> = habitsDao.selectAllHabits().map {
        it.map { habitDB ->
            habitMapper.habitDataToHabitDomain(habitDB)
        }
    }

    override fun selectGoodHabitsFromDb(): Flow<List<Habit>> = habitsDao.selectGoodHabits().map {
        it.map { habitDB ->
            habitMapper.habitDataToHabitDomain(habitDB)
        }
    }

    override fun selectBadHabitsFromDb(): Flow<List<Habit>> = habitsDao.selectBadHabits().map {
        it.map { habitDB ->
            habitMapper.habitDataToHabitDomain(habitDB)
        }
    }

    override suspend fun insertHabits(habits: List<Habit>) {
        if (habits.isNotEmpty()) {
            val habitsData = habits.map {
                habitMapper.habitDomainToHabitData(it)
            }
            habitsData.forEach {
                habitsDao.insertHabit(it)
            }
        }
        return
    }

    override suspend fun insertHabit(habit: Habit) =
        habitsDao.insertHabit(habitMapper.habitDomainToHabitData(habit))


    override suspend fun updateHabit(habit: Habit) =
        habitsDao.updateHabit(habitMapper.habitDomainToHabitData(habit))

    override suspend fun deleteHabit(habit: Habit) =
        habitsDao.deleteHabit(habitMapper.habitDomainToHabitData(habit))


    override suspend fun deleteAllHabitsFromDb() {
        habitsDao.deleteAllHabits()
    }
    //--------------DATABASE--------------------

    //-----------------API---------------------
    override suspend fun getHabitsFromApi(): List<Habit> {
        var habits = cloudRepository.getAllHabits().map {
            habitMapper.habitDataToHabitDomain(it)
        }
        return habits
    }


    override suspend fun deleteHabitInApi(habit: Habit) =
        cloudRepository.deleteHabit(habitMapper.habitDomainToHabitData(habit))

    override suspend fun deleteHabitsInApi(habits: List<Habit>) =
        cloudRepository.deleteHabits(habits.map {
            habitMapper.habitDomainToHabitData(it)
        })


    override suspend fun putHabitsInApi(habits: List<Habit>) =
        cloudRepository.putHabits(habits.map {
            habitMapper.habitDomainToHabitData(it)
        })


    override suspend fun putHabitInApi(habit: Habit) =
        cloudRepository.putHabit(habitMapper.habitDomainToHabitData(habit))


    override suspend fun postHabitsDoneInApi(
        habitsFromApi: List<Habit>,
        habitsFromDb: List<Habit>
    ) =
        cloudRepository.postHabitsDone(habitsFromApi.map {
            habitMapper.habitDomainToHabitData(it)
        }, habitsFromDb.map {
            habitMapper.habitDomainToHabitData(it)
        })

    //-----------------API---------------------

    //----------------SORTED-------------------
    @ExperimentalCoroutinesApi
    override fun getSortedHabits(): Flow<List<Habit>> {
        return sortedHabits.sortedHabits
    }

    @ExperimentalCoroutinesApi
    override fun setSortedHabits(newHabits: List<Habit>) {
        sortedHabits.sortedHabits.value = newHabits
    }
    //----------------SORTED-------------------
}
