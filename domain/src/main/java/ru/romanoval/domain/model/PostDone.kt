package ru.romanoval.domain.model

import com.google.gson.annotations.SerializedName

data class PostDone (
    @SerializedName("habit_uid")
    var uid: String,
    @SerializedName("date")
    var date: Long
)