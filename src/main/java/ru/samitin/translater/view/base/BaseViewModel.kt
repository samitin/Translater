package ru.samitin.translater.view.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import ru.samitin.translater.model.data.state.AppState
import ru.samitin.translater.rx.ISchedulerProvider
import ru.samitin.translater.rx.SchedulerProvider

abstract class BaseViewModel<T:AppState> (
    // Обратите внимание, что мы добавили инстанс LiveData
    protected val liveDataForViewToObserve :MutableLiveData<T> = MutableLiveData(),
    protected val compositeDisposable : CompositeDisposable = CompositeDisposable(),
    protected val shedulerProvider: ISchedulerProvider = SchedulerProvider()): ViewModel(){
    // Наследуемся от ViewModel
    // Метод, благодаря которому Activity подписывается на изменение данных,
    // возвращает LiveData, через которую и передаются данные
    open fun getData(world:String,isOnline:Boolean):LiveData<T> = liveDataForViewToObserve
    // Единственный метод класса ViewModel, который вызывается перед
    // уничтожением Activity
    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}