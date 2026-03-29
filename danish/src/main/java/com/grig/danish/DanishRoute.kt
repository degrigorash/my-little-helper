package com.grig.danish

import kotlinx.serialization.Serializable

sealed class DanishRoute {

    @Serializable
    data object DanishHome : DanishRoute()
}