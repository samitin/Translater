package ru.samitin.translater.view.main.interactor


import ru.samitin.core.viewModel.Interactor
import ru.samitin.model.SearchResultDto
import ru.samitin.model.state.AppState
import ru.samitin.repository.Repository
import ru.samitin.repository.RepositoryLocal
import ru.samitin.translater.utils.mapSearchResultToResult


class MainInteractor(
    private val repositoryRemote: Repository<List<SearchResultDto>>,
    private val repositoryLocal: RepositoryLocal<List<SearchResultDto>>
) : Interactor<AppState> {
    // Добавляем suspend
    override suspend fun getData(word: String, fromRemoteSource: Boolean): AppState {
        val appState : AppState
        // Теперь полученное слово мы сохраняем в БД. Сделать это нужно именно
        // здесь, в соответствии с принципами чистой архитектуры: интерактор
        // обращается к репозиторию
        if (fromRemoteSource){
            appState = AppState.Success(mapSearchResultToResult( repositoryRemote.getData(word)))
            repositoryLocal.saveToDB(appState)
        }else{
            appState = AppState.Success(mapSearchResultToResult(repositoryLocal.getData(word)))
        }
        return appState
    }
}