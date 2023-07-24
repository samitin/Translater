package ru.samitin.translater.model.dataSource.retrofit



import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query
import ru.samitin.translater.model.data.DataModel

interface ApiService {

    // Обратите внимание, что метод теперь возвращает Deferred
    @GET("words/search")
    fun searchAsync(@Query("search")wordToSearch:String): Deferred<List<DataModel>>
}