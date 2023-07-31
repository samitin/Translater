package ru.samitin.translater.model.dataSource.dataSource

import ru.samitin.translater.model.data.state.AppState

interface DataSourceLocal<T> : DataSource<T> {

    suspend fun saveToDB(appState: AppState)
}