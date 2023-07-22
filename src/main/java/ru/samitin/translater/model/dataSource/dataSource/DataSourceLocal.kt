package ru.samitin.translater.model.dataSource.dataSource

import io.reactivex.Observable
import ru.samitin.translater.model.data.DataModel
import ru.samitin.translater.model.dataSource.room.RoomDataBaseImplementation

// Для локальных данных используется Room
class DataSourceLocal(private val localProvider: RoomDataBaseImplementation = RoomDataBaseImplementation()
) : DataSource<List<DataModel>> {
    override fun getData(word: String): Observable<List<DataModel>> {
        return localProvider.getData(word)
    }

}