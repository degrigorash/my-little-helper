package com.grig.danish.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "nouns")
data class Noun(
    @PrimaryKey val id: Int,
    val english: String,
    val alt: List<String>,
    val danish: String,
    @SerialName("the") @ColumnInfo(name = "the_form") val theForm: String,
    val plural: String,
    val thePlural: String,
    val folder: String,
    val notes: String?
)
