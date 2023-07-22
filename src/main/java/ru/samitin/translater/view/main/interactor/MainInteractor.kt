package ru.samitin.translater.view.main.interactor

import io.reactivex.Observable
import ru.samitin.translater.model.data.DataModel
import ru.samitin.translater.presenter.Interactor
import ru.samitin.translater.model.repository.Repository
import ru.samitin.translater.model.data.state.AppState

class MainInteractor(
    private val remoteRepository: Repository<List<DataModel>>,
    private val localRepository: Repository<List<DataModel>>
): Interactor<AppState> {
    override fun getData(word: String, fromRemoteSource: Boolean): Observable<AppState> {
        return if (fromRemoteSource)
            remoteRepository.getData(word).map { AppState.Success(it) }
        else
            localRepository.getData(word).map { AppState.Success(it) }
    }
}