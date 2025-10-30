package com.grig.mylittlehelper

import kotlinx.serialization.Serializable

sealed class Route {

    @Serializable
    data object Home : Route()
}