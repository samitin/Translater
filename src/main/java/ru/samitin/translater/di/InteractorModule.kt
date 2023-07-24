package ru.samitin.translater.di

import dagger.Module
import dagger.Provides
import ru.samitin.translater.model.data.DataModel
import ru.samitin.translater.model.repository.Repository
import ru.samitin.translater.view.main.interactor.MainInteractor
import javax.inject.Named


@Module
class InteractorModule {

    @Provides
    internal fun provideInteractor(
        @Named(NAME_REMOTE) repositoryRemote: Repository<List<DataModel>>,
        @Named(NAME_LOCAL) repositoryLocal: Repository<List<DataModel>>
    ) = MainInteractor(repositoryRemote, repositoryLocal)
}
