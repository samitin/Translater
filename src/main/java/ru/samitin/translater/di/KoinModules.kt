package ru.samitin.translater.di



import androidx.room.Room
import org.koin.dsl.module
import ru.samitin.translater.model.data.DataModel
import ru.samitin.translater.model.dataSource.retrofit.RetrofitImplementation
import ru.samitin.translater.model.dataSource.room.HistoryDataBase
import ru.samitin.translater.model.dataSource.room.RoomDataBaseImplementation
import ru.samitin.translater.model.repository.Repository
import ru.samitin.translater.model.repository.RepositoryImplementation
import ru.samitin.translater.model.repository.RepositoryImplementationLocal
import ru.samitin.translater.model.repository.RepositoryLocal
import ru.samitin.translater.view.history.interactor.HistoryInteractor
import ru.samitin.translater.view.history.viewModel.HistoryViewModel
import ru.samitin.translater.view.main.interactor.MainInteractor
import ru.samitin.translater.view.main.viewModel.MainViewModel

// Для удобства создадим две переменные: в одной находятся зависимости,
// используемые во всём приложении, во второй - зависимости конкретного экрана
val application = module{
    // single указывает, что БД должна быть в единственном экземпляре
    single { Room.databaseBuilder(get(),HistoryDataBase::class.java,"HistoryDB").build() }
    // Получаем DAO
    single { get<HistoryDataBase>().historyDao() }
    // Функция single сообщает Koin, что эта зависимость должна храниться
    // в виде синглтона (в Dagger есть похожая аннотация)
    // Аннотация named выполняет аналогичную Dagger функцию
    single<Repository<List<DataModel>>> { RepositoryImplementation(RetrofitImplementation()) }
    single<RepositoryLocal<List<DataModel>>> { RepositoryImplementationLocal(RoomDataBaseImplementation(get()))}
}

// Функция factory сообщает Koin, что эту зависимость нужно создавать каждый
// раз заново, что как раз подходит для Activity и её компонентов.
val mainScreen = module {
    factory { MainViewModel(get()) }
    factory { MainInteractor(get(), get()) }
}

val historyScreen = module {
    factory { HistoryViewModel(get()) }
    factory { HistoryInteractor(get(), get()) }
}