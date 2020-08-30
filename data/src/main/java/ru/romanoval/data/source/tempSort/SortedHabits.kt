package ru.romanoval.data.source.tempSort

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import ru.romanoval.domain.model.Habit

object SortedHabits {

    @ExperimentalCoroutinesApi
    var sortedHabits: MutableStateFlow<List<Habit>> = MutableStateFlow(emptyList())

}