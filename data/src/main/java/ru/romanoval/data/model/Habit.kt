package ru.romanoval.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.*
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.lang.reflect.Type

@Entity
data class Habit(
    var uid: String?,
    @PrimaryKey(autoGenerate = true)
    var bdId: Int?,
    var title: String,
    var description: String,
    var priority: Int,
    var type: Int,
    var count: Int,
    var frequency: Int,
    var color: Int,
    var date: Long,
    @TypeConverters(ListConverter::class)
    var done_dates: MutableList<Long>
) : Serializable {

    class ListConverter {
        @TypeConverter
        fun fromHabits(habits: MutableList<Long>): String {
            return habits.joinToString()
        }

        @TypeConverter
        fun toHabits(data: String): MutableList<Long> {
            return if (data != "") {
                data.split(", ").map { it.toLong() }.toMutableList()
            } else {
                mutableListOf()
            }
        }
    }

    class HabitJsonSerializer : JsonSerializer<Habit> {
        override fun serialize(
            src: Habit?,
            typeOfSrc: Type?,
            context: JsonSerializationContext?
        ): JsonElement {
            return JsonObject().apply {
                addProperty("uid", src?.uid)
                addProperty("title", src?.title)
                addProperty("description", src?.description)
                addProperty("priority", src?.priority)
                addProperty("type", src?.type)
                addProperty("count", src?.count)
                addProperty("frequency", src?.frequency)
                addProperty("color", src?.color)
                addProperty("date", src?.date)
            }
        }
    }

    data class DoneDates(
        @SerializedName("done_dates")
        var done_dates: MutableList<Long>
    )

    class HabitJsonDeserializer : JsonDeserializer<Habit> {
        override fun deserialize(
            json: JsonElement,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): Habit {
            val list = GsonBuilder().create()
                .fromJson<DoneDates>(
                    json.asJsonObject,
                    DoneDates::class.java
                ).done_dates
            println(json.asJsonObject.get("done_dates").asJsonArray)
            return Habit(
                bdId = null,
                uid = json.asJsonObject.get("uid").asString,
                title = json.asJsonObject.get("title").asString,
                description = json.asJsonObject.get("description").asString,
                priority = json.asJsonObject.get("priority").asInt,
                type = json.asJsonObject.get("type").asInt,
                count = json.asJsonObject.get("count").asInt,
                frequency = json.asJsonObject.get("frequency").asInt,
                color = json.asJsonObject.get("color").asInt,
                date = json.asJsonObject.get("date").asLong,
                done_dates = list.toMutableList()
            )

        }
    }

}