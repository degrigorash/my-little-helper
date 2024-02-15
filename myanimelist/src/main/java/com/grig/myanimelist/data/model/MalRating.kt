package com.grig.myanimelist.data.model

import androidx.compose.ui.graphics.Color
import kotlinx.serialization.SerialName

enum class MalRating(val color: Color) {
    @SerialName("g")
    G(Color.Cyan),
    @SerialName("pg")
    PG(Color.Green),
    @SerialName("pg_13")
    PG13(Color.Blue),
    @SerialName("r")
    R(Color.Yellow),
    @SerialName("r+")
    RPlus(Color.Magenta),
    @SerialName("rx")
    Rx(Color.Red)
}