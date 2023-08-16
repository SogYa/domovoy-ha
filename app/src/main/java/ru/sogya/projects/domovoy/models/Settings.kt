package ru.sogya.projects.domovoy.models

import ru.sogya.projects.domovoy.R


data class Settings(
    val id: Int,
    val label: String,
    val desc: String,
    val navigationId: Int?,
    val resourceId:Int
) {
    companion object {
        private const val APP_LOCKER = 0
        const val LOG_OUT = 3
        private const val MAP = 1
        private const val CONTACTS = 2
        val settingsList = listOf(
            Settings(
                APP_LOCKER,
                "Пин-код",
                "Пин-код для доступа к серверу ",
                R.id.action_settingsFragment_to_appLockFragment,
                R.drawable.ic_baseline_lock_24
            ), Settings(
                CONTACTS,
                "Обратная связь",
                "Связь с разработчиком",
                R.id.action_settingsFragment_to_contactsFragment,
                R.drawable.baseline_people_alt_24
            ), Settings(
                LOG_OUT,
                "Выйти",
                "Отключиться от сервера",
                null,
                R.drawable.baseline_logout
            )
        )
    }
}