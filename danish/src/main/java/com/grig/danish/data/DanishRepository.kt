package com.grig.danish.data

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DanishRepository @Inject constructor(
    private val danishService: DanishService
)
