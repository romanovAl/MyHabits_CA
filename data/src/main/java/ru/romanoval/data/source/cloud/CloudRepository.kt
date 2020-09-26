package ru.romanoval.data.source.cloud

import ru.romanoval.data.model.Habit
import ru.romanoval.data.restful.ApiService
import ru.romanoval.domain.model.PostDone
import ru.romanoval.domain.model.Uid

class CloudRepository(private val service: ApiService) : BaseCloudRepository {

    override suspend fun getAllHabits(): List<Habit> {
        return service.getHabits(
            "application/json",
            "5cbb174f-cd85-4ad0-91d4-93be374c9883"
        )
    }

    override suspend fun deleteHabit(habit: Habit) {
        val delete = Uid(habit.uid!!)
        return service.deleteHabit(
            "application/json",
            "5cbb174f-cd85-4ad0-91d4-93be374c9883",
            "application/json",
            delete
        )
    }

    override suspend fun putHabit(habit: Habit) {
        return service.putHabit(
            "application/json",
            "5cbb174f-cd85-4ad0-91d4-93be374c9883",
            "application/json",
            habit
        )
    }

    override suspend fun deleteHabits(habits: List<Habit>) {
        return habits.forEach {
            val delete = Uid(it.uid!!)
            service
                .deleteHabit(
                    "application/json",
                    "5cbb174f-cd85-4ad0-91d4-93be374c9883",
                    "application/json",
                    delete
                )
        }
    }

    override suspend fun putHabits(habits: List<Habit>) {
        return habits
            .forEach {
                it.uid = null
                service
                    .putHabit(
                        "application/json",
                        "5cbb174f-cd85-4ad0-91d4-93be374c9883",
                        "application/json",
                        it
                    )
            }
    }

    override suspend fun postHabitsDone(habitsFromApi: List<Habit>, habitsFromDb: List<Habit>) {
        val habits = habitsFromDb.toMutableList()
        for (i in 0 until habits.size) {
            habits[i].uid = habitsFromApi[i].uid
        }

        habits.forEach { habit ->
            habit.done_dates.forEach {
                val postDone = PostDone(habit.uid!!, it)
                service
                    .postHabitDone(
                        "application/json",
                        "5cbb174f-cd85-4ad0-91d4-93be374c9883",
                        "application/json",
                        postDone
                    )
            }
        }
    }


}