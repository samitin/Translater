package ru.samitin.translater.view.base

import ru.samitin.translater.model.data.state.AppState

// Нижний уровень. View знает о контексте и фреймворке
interface View {
    // View имеет только один метод, в который приходит некое состояние приложения
    fun renderData(appState: AppState)
}