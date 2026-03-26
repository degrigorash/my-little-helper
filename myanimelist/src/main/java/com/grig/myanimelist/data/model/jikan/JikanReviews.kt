package com.grig.myanimelist.data.model.jikan

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class JikanReviewsResponse(
    @SerialName("pagination")
    val pagination: JikanPagination,
    @SerialName("data")
    val data: List<JikanReview>
)

@Serializable
data class JikanPagination(
    @SerialName("last_visible_page")
    val lastVisiblePage: Int,
    @SerialName("has_next_page")
    val hasNextPage: Boolean
)

@Serializable
data class JikanReview(
    @SerialName("mal_id")
    val malId: Int,
    @SerialName("date")
    val date: String,
    @SerialName("review")
    val review: String,
    @SerialName("score")
    val score: Int,
    @SerialName("tags")
    val tags: List<String> = emptyList(),
    @SerialName("is_spoiler")
    val isSpoiler: Boolean = false,
    @SerialName("is_preliminary")
    val isPreliminary: Boolean = false,
    @SerialName("reactions")
    val reactions: JikanReviewReactions? = null,
    @SerialName("user")
    val user: JikanReviewUser
)

@Serializable
data class JikanReviewReactions(
    @SerialName("overall")
    val overall: Int = 0,
    @SerialName("nice")
    val nice: Int = 0,
    @SerialName("love_it")
    val loveIt: Int = 0,
    @SerialName("funny")
    val funny: Int = 0,
    @SerialName("confusing")
    val confusing: Int = 0,
    @SerialName("informative")
    val informative: Int = 0,
    @SerialName("well_written")
    val wellWritten: Int = 0,
    @SerialName("creative")
    val creative: Int = 0
)

@Serializable
data class JikanReviewUser(
    @SerialName("username")
    val username: String,
    @SerialName("images")
    val images: JikanReviewUserImages? = null
)

@Serializable
data class JikanReviewUserImages(
    @SerialName("jpg")
    val jpg: JikanReviewImageFormat? = null,
    @SerialName("webp")
    val webp: JikanReviewImageFormat? = null
)

@Serializable
data class JikanReviewImageFormat(
    @SerialName("image_url")
    val imageUrl: String? = null
)
