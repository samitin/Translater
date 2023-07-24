package ru.samitin.translater.model.dataSource.retrofit

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Observable
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.samitin.translater.model.data.DataModel
import ru.samitin.translater.model.dataSource.dataSource.DataSource

class RetrofitImplementation : DataSource<List<DataModel>> {
    // Добавляем suspend и .await()
    override suspend fun getData(word: String): List<DataModel> {
        return getService(BaseInterceptor.interceptor).searchAsync(word).await()
    }

    private fun getService(interceptor: Interceptor) : ApiService{
        return createRetrofit(interceptor).create(ApiService::class.java)
    }
// Обратите внимание на Builder: в addCallAdapterFactory теперь передаётся
// CoroutineCallAdapterFactory() которая позволяет Retrofit работать с
// корутинами. Для ее использования нужно прописать для Ретрофита зависимость
// вместо той, которая была для Rx: implementation
// 'com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:0.9.2'
    private fun createRetrofit(interceptor: Interceptor) : Retrofit{
        return Retrofit.Builder()
            .baseUrl(BASE_URL_LOCATIONS)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(createOkHttpClient(interceptor))
            .build()
    }
    private fun createOkHttpClient(interceptor: Interceptor) : OkHttpClient{
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(interceptor)
        httpClient.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        return httpClient.build()
    }

    companion object{
        private const val BASE_URL_LOCATIONS =
            "https://dictionary.skyeng.ru/api/public/v1/"
    }
}