package com.grig.myanimelist.data.model

import kotlinx.serialization.Serializable

@Serializable
data class MalGenre(
    val id: Int,
    val name: String
)
