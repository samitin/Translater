package ru.samitin.translater.view.history.interactor

import ru.samitin.translater.interactor.Interactor
import ru.samitin.translater.model.data.DataModel
import ru.samitin.translater.model.data.state.AppState
import ru.samitin.translater.model.repository.Repository
import ru.samitin.translater.model.repository.RepositoryLocal

// Класс мало чем отличается от интерактора, который мы уже описывали
class HistoryInteractor(
    private val repositoryRemote: Repository<List<DataModel>>,
    private val repositoryLocal: RepositoryLocal<List<DataModel>>
) : Interactor<AppState> {
    override suspend fun getData(word: String, fromRemoteSource: Boolean):
            AppState {
        return AppState.Success(
            if (fromRemoteSource) {
                repositoryRemote
            } else {
                repositoryLocal
            }.getData(word)
        )
    }
}