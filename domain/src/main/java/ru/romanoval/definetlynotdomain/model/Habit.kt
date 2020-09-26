package ru.romanoval.domain.model

import java.io.Serializable

data class Habit(
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
) : Serializable