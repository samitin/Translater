package ru.samitin.translater.view.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import ru.samitin.translater.model.data.state.AppState
import ru.samitin.translater.rx.SchedulerProvider

abstract class BaseViewModel<T:AppState> (
    // Обратите внимание, что мы добавили инстанс LiveData
    protected open val liveDataForViewToObserve: MutableLiveData<T> = MutableLiveData(),
    protected open val compositeDisposable: CompositeDisposable = CompositeDisposable(),
    protected open val schedulerProvider: SchedulerProvider = SchedulerProvider()
) : ViewModel() {
    // Наследуемся от ViewModel
    // Метод, благодаря которому Activity подписывается на изменение данных,
    // возвращает LiveData, через которую и передаются данные
    abstract fun getData(word: String, isOnline: Boolean)
    // Единственный метод класса ViewModel, который вызывается перед
    // уничтожением Activity
    override fun onCleared() {
        compositeDisposable.clear()
    }
}