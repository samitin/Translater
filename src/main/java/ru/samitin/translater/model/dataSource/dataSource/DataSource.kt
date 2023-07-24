package ru.samitin.translater.model.dataSource.dataSource

import io.reactivex.Observable

// Источник данных для репозитория (Интернет, БД и т. п.)
interface DataSource<T> {
    suspend fun  getData(word:String):T
}