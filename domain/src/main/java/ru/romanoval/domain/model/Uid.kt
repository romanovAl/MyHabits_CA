package ru.romanoval.domain.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Uid(
    @SerializedName("uid")
    val uid: String
) :Serializable