package ru.romanoval.data.source.cloud

import ru.romanoval.domain.model.Habit

interface BaseCloudRepository {

    suspend fun getAllHabits(): List<Habit>

    suspend fun deleteHabit(habit: Habit)

    suspend fun putHabit(habit: Habit)

    suspend fun deleteHabits(habits: List<Habit>)

    suspend fun putHabits(habits: List<Habit>)

    suspend fun postHabitsDone(habitsFromApi: List<Habit>, habitsFromDb: List<Habit>)
}