package ru.romanoval.data.restful

import retrofit2.http.*
import ru.romanoval.data.model.Habit

import ru.romanoval.domain.model.PostDone
import ru.romanoval.domain.model.Uid

interface ApiService {

    @GET("api/habit")
    suspend fun getHabits(
        @Header("accept") accept: String,
        @Header("Authorization") authorization: String
    ) :List<Habit>

    @HTTP(method = "DELETE", path = "api/habit", hasBody = true)
    suspend fun deleteHabit(
        @Header("accept") accept: String,
        @Header("Authorization") authorization: String,
        @Header("Content-Type") contentType: String,
        @Body uid: Uid
    )

    @POST("api/habit_done")
    suspend fun postHabitDone(
        @Header("accept") accept: String,
        @Header("Authorization") authorization: String,
        @Header("Content-Type") contentType: String,
        @Body postDone: PostDone
    )

    @PUT("api/habit")
    suspend fun putHabit(
        @Header("accept") accept: String,
        @Header("Authorization") authorization: String,
        @Header("Content-Type") contentType: String,
        @Body habit: Habit
    )

}