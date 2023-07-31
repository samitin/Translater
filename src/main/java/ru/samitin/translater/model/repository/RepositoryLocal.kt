package ru.samitin.translater.model.repository

import ru.samitin.translater.model.data.state.AppState

// Наследуемся от Repository и добавляем нужный метод
interface RepositoryLocal<T> : Repository<T> {
    suspend fun saveToDB(appState: AppState)
}