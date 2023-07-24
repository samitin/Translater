package ru.samitin.translater.model.repository

import io.reactivex.Observable
import ru.samitin.translater.model.data.DataModel
import ru.samitin.translater.model.dataSource.dataSource.DataSource

class RepositoryImplementation(private val dataSource: DataSource<List<DataModel>>):
Repository<List<DataModel>>{
    // Репозиторий возвращает данные, используя dataSource (локальный или
    // внешний)
    override suspend fun getData(word: String): List<DataModel> {
        return dataSource.getData(word)
    }
}