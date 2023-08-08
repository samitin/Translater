package ru.samitin.translater.view.main.interactor


import ru.samitin.core.viewModel.Interactor
import ru.samitin.model.DataModel
import ru.samitin.model.state.AppState
import ru.samitin.repository.Repository
import ru.samitin.repository.RepositoryLocal


class MainInteractor(
    private val repositoryRemote: Repository<List<DataModel>>,
    private val repositoryLocal: RepositoryLocal<List<DataModel>>
) : Interactor<AppState> {
    // Добавляем suspend
    override suspend fun getData(word: String, fromRemoteSource: Boolean): AppState {
        val appState : AppState
        // Теперь полученное слово мы сохраняем в БД. Сделать это нужно именно
        // здесь, в соответствии с принципами чистой архитектуры: интерактор
        // обращается к репозиторию
        if (fromRemoteSource){
            appState = AppState.Success(repositoryRemote.getData(word))
            repositoryLocal.saveToDB(appState)
        }else{
            appState = AppState.Success(repositoryLocal.getData(word))
        }
        return appState
    }
}