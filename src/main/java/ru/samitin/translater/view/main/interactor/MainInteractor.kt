package ru.samitin.translater.view.main.interactor

import io.reactivex.Observable
import ru.samitin.translater.di.NAME_LOCAL
import ru.samitin.translater.di.NAME_REMOTE
import ru.samitin.translater.model.data.DataModel
import ru.samitin.translater.interactor.Interactor
import ru.samitin.translater.model.repository.Repository
import ru.samitin.translater.model.data.state.AppState
import javax.inject.Inject
import javax.inject.Named

class MainInteractor @Inject constructor(
    @Named(NAME_REMOTE) val repositoryRemote: Repository<List<DataModel>>,
    @Named(NAME_LOCAL) val repositoryLocal: Repository<List<DataModel>>
) : Interactor<AppState> {
    override fun getData(word: String, fromRemoteSource: Boolean): Observable<AppState> {
        return if (fromRemoteSource)
            repositoryRemote.getData(word).map { AppState.Success(it) }
        else
            repositoryLocal.getData(word).map { AppState.Success(it) }
    }
}