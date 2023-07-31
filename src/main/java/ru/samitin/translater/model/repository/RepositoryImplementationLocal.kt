package ru.samitin.translater.model.repository

import ru.samitin.translater.model.data.DataModel
import ru.samitin.translater.model.data.state.AppState
import ru.samitin.translater.model.dataSource.dataSource.DataSourceLocal

// RepositoryImplementationLocal теперь содержит два метода, наследуется от
// RepositoryLocal и в конструктор получает инстанс DataSourceLocal
class RepositoryImplementationLocal (
    private val dataSourceLocal: DataSourceLocal<List<DataModel>>
) : RepositoryLocal<List<DataModel>>{

    override suspend fun getData(word: String): List<DataModel> {
        return dataSourceLocal.getData(word)
    }
    override suspend fun saveToDB(appState: AppState) {
        dataSourceLocal.saveToDB(appState)
    }

}