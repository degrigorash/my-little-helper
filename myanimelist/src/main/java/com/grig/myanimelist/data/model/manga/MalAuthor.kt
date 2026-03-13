package com.grig.myanimelist.data.model.manga

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MalAuthorNode(
    @SerialName("node")
    val author: MalAuthor,
    @SerialName("role")
    val role: String = ""
)

@Serializable
data class MalAuthor(
    @SerialName("id")
    val id: Int,
    @SerialName("first_name")
    val firstName: String = "",
    @SerialName("last_name")
    val lastName: String = ""
) {
    val displayName: String
        get() = listOf(firstName, lastName).filter { it.isNotBlank() }.joinToString(" ")
}
