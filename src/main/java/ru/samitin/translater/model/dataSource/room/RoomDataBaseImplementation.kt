package ru.samitin.translater.model.dataSource.room

import io.reactivex.Observable
import ru.samitin.translater.model.data.DataModel
import ru.samitin.translater.model.dataSource.dataSource.DataSource

class RoomDataBaseImplementation : DataSource<List<DataModel>> {
    override suspend fun getData(word: String): List<DataModel> {
        TODO("Not yet implemented")
    }
}