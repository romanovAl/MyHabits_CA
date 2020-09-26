package ru.romanoval.data.mapper

import ru.romanoval.data.model.Habit

class HabitMapper {

    fun habitDataToHabitDomain(habit: Habit)  =
        ru.romanoval.domain.model.Habit(
            uid = habit.uid,
            bdId = habit.bdId,
            title = habit.title,
            description = habit.description,
            priority = habit.priority,
            type = habit.type,
            count = habit.count,
            frequency =  habit.frequency,
            color = habit.color,
            date = habit.date,
            done_dates = habit.done_dates
        )

    fun habitDomainToHabitData(habit: ru.romanoval.domain.model.Habit) =
        Habit(
            uid = habit.uid,
            bdId = habit.bdId,
            title = habit.title,
            description = habit.description,
            priority = habit.priority,
            type = habit.type,
            count = habit.count,
            frequency = habit.frequency,
            color = habit.color,
            date = habit.date,
            done_dates = habit.done_dates
        )
}