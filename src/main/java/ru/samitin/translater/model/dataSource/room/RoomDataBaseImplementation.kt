package ru.samitin.translater.model.dataSource.room

import io.reactivex.Observable
import ru.samitin.translater.model.data.DataModel
import ru.samitin.translater.model.data.state.AppState
import ru.samitin.translater.model.dataSource.dataSource.DataSource
import ru.samitin.translater.model.dataSource.dataSource.DataSourceLocal
import ru.samitin.translater.utils.convertDataModelSuccessToEntity
import ru.samitin.translater.utils.mapHistoryEntityToSearchResult

// Теперь наш локальный репозиторий работает. Передаём в конструктор
// HistoryDao (вспоминаем в модуле Koin RoomDataBaseImplementation(get())).
class RoomDataBaseImplementation(private val historyDao: HistoryDao)
    : DataSourceLocal<List<DataModel>> {


    // Возвращаем список всех слов в виде понятного для Activity
    // List<SearchResult>
    override suspend fun getData(word: String): List<DataModel> {
        // Метод mapHistoryEntityToSearchResult описан во вспомогательном
        // классе SearchResultParser, в котором есть и другие методы для
        // трансформации данных
        return mapHistoryEntityToSearchResult(historyDao.all())
    }
    // Метод сохранения слова в БД. Он будет использоваться в интеракторе
    override suspend fun saveToDB(appState: AppState) {
        convertDataModelSuccessToEntity(appState)?.let {
            historyDao.insert(it)
        }
    }
}