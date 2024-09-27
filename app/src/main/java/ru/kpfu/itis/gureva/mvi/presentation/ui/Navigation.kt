package ru.kpfu.itis.gureva.mvi.presentation.ui

import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable data object Home : Route
    @Serializable data class Group(val id: Int) : Route
}
