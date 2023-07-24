package ru.samitin.translater.view.app

import android.app.Application
import org.koin.core.context.startKoin
import ru.samitin.translater.di.application
import ru.samitin.translater.di.mainScreen


// Обратите внимание на dispatchingAndroidInjector и интерфейс Dagger
// HasAndroidInjector: мы переопределяем его метод androidInjector. Они
// нужны для внедрения зависимостей в Activity. По своей сути — это вспомогательные
//методы для разработчиков под Андроид для эффективного внедрения компонентов
//платформы, таких как Активити, Сервис и т. п.
class TranslatorApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(mainScreen, application)
        }
    }
}