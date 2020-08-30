package ru.romanoval.data.source.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.romanoval.domain.model.Habit

@Database(entities = [Habit::class], version = AppDatabase.VERSION)
@TypeConverters(Habit.ListConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun habitsDao(): HabitsDao

    companion object{
        const val DB_NAME = "habits.db"
        const val VERSION = 1
    }

}