package com.example.navi

import java.io.Serializable


data class NaviRouteData(
    val selectedGoalItem: Item?,
    val typeSetTime: String,
    val setTime: String,
    val energyUsageMode: String,
) : Serializable