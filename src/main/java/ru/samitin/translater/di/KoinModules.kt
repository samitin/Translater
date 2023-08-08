package ru.samitin.translater.di



import androidx.room.Room
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.dsl.scoped
import ru.samitin.history.view.history.HistoryInteractor
import ru.samitin.history.view.history.HistoryViewModel
import ru.samitin.model.DataModel
import ru.samitin.repository.Repository
import ru.samitin.repository.RepositoryImplementation
import ru.samitin.repository.RepositoryImplementationLocal
import ru.samitin.repository.RepositoryLocal
import ru.samitin.repository.dataSource.retrofit.RetrofitImplementation
import ru.samitin.repository.dataSource.room.HistoryDataBase
import ru.samitin.repository.dataSource.room.RoomDataBaseImplementation
import ru.samitin.translater.view.main.interactor.MainInteractor
import ru.samitin.translater.view.main.screen.MainActivity
import ru.samitin.translater.view.main.viewModel.MainViewModel

// Для удобства создадим две переменные: в одной находятся зависимости,
// используемые во всём приложении, во второй - зависимости конкретного экрана
val application = module{
    // single указывает, что БД должна быть в единственном экземпляре
    single { Room.databaseBuilder(get(), HistoryDataBase::class.java,"HistoryDB").build() }
    // Получаем DAO
    single { get<HistoryDataBase>().historyDao() }
    // Функция single сообщает Koin, что эта зависимость должна храниться
    // в виде синглтона (в Dagger есть похожая аннотация)
    // Аннотация named выполняет аналогичную Dagger функцию
    single<Repository<List<DataModel>>> { RepositoryImplementation(RetrofitImplementation()) }
    single<RepositoryLocal<List<DataModel>>> { RepositoryImplementationLocal(
        RoomDataBaseImplementation(get())
    )
    }
}

// Функция factory сообщает Koin, что эту зависимость нужно создавать каждый
// раз заново, что как раз подходит для Activity и её компонентов.
val mainScreen = module {
    factory { MainInteractor(get(), get())  }
    viewModel{ MainViewModel(get()) }
}

val historyScreen = module {
    factory { HistoryInteractor(get(), get()) }
    viewModel { HistoryViewModel(get()) }
}