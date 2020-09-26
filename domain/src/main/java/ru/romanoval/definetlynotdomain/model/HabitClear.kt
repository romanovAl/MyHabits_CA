package ru.romanoval.domain.model

data class HabitClear(
    var uid: String?,
    var bdId: Int?,
    var title: String,
    var description: String,
    var priority: Int,
    var type: Int,
    var count: Int,
    var frequency: Int,
    var color: Int,
    var date: Long,
    var done_dates: MutableList<Long>
)