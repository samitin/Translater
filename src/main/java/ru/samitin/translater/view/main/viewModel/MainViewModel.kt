package ru.samitin.translater.view.main.viewModel

import androidx.lifecycle.LiveData
import io.reactivex.observers.DisposableObserver
import ru.samitin.translater.model.data.state.AppState
import ru.samitin.translater.model.dataSource.dataSource.DataSourceLocal
import ru.samitin.translater.model.dataSource.dataSource.DataSourceRemote
import ru.samitin.translater.model.repository.RepositoryImplementation
import ru.samitin.translater.view.base.BaseViewModel
import ru.samitin.translater.view.main.interactor.MainInteractor

class MainViewModel(private val interactor: MainInteractor = MainInteractor(
    RepositoryImplementation(DataSourceRemote()),
    RepositoryImplementation(DataSourceLocal())
)
) : BaseViewModel<AppState>() {
    // В этой переменной хранится последнее состояние Activity
    private var appState : AppState ?= null

    // Переопределяем метод из BaseViewModel
    override fun getData(world: String, isOnline: Boolean): LiveData<AppState> {
        compositeDisposable.add(
            interactor.getData(world,isOnline).
                    subscribeOn(shedulerProvider.io()).
                    observeOn(shedulerProvider.ui()).
                    doOnSubscribe{liveDataForViewToObserve.value = AppState.Loading(null)}.
                    subscribeWith(getObserver())
        )
        return super.getData(world, isOnline)
    }

    private fun getObserver() : DisposableObserver<AppState>{
        return object: DisposableObserver<AppState>(){
            // Данные успешно загружены; сохраняем их и передаем во View (через
            // LiveData). View сама разберётся, как их отображать
            override fun onNext(t: AppState) {
                appState = t
                liveDataForViewToObserve.value = t
            }
            // В случае ошибки передаём её в Activity таким же образом через LiveData
            override fun onError(e: Throwable) {
                liveDataForViewToObserve.value = AppState.Error(e)
            }

            override fun onComplete() {
            }

        }
    }
}