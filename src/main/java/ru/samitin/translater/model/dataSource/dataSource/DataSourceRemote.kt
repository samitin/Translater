package ru.samitin.translater.model.dataSource.dataSource

import io.reactivex.Observable
import ru.samitin.translater.model.data.DataModel
import ru.samitin.translater.model.dataSource.retrofit.RetrofitImplementation

class DataSourceRemote(private val remoteProvider: RetrofitImplementation = RetrofitImplementation()
) : DataSource<List<DataModel>> {
    override fun getData(word: String): Observable<List<DataModel>> {
        return remoteProvider.getData(word)
    }
}

