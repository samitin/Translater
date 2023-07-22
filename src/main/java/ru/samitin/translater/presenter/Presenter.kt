package ru.samitin.translater.presenter

import ru.samitin.translater.model.data.state.AppState
import ru.samitin.translater.view.base.View

// На уровень выше находится презентер, который уже ничего не знает ни о контексте, ни о фреймворке
interface Presenter<T : AppState,V : View> {

    fun attachView(view : V)

    fun detachView(view : V)
    // Получение данных с флагом isOnline(из Интернета или нет)
    fun getData(word:String,isOnline:Boolean)
}