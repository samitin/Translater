package ru.samitin.translater.view.main.interactor

import io.reactivex.Observable
import ru.samitin.translater.model.data.DataModel
import ru.samitin.translater.interactor.Interactor
import ru.samitin.translater.model.repository.Repository
import ru.samitin.translater.model.data.state.AppState


class MainInteractor(
    private val repositoryRemote: Repository<List<DataModel>>,
    private val repositoryLocal: Repository<List<DataModel>>
) : Interactor<AppState> {
    // Добавляем suspend
    override suspend fun getData(word: String, fromRemoteSource: Boolean): AppState {
        return AppState.Success(
            if (fromRemoteSource) {
                repositoryRemote
            } else {
                repositoryLocal
            }.getData(word)
        )
    }
}