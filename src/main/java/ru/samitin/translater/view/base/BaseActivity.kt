package ru.samitin.translater.view.base

import androidx.appcompat.app.AppCompatActivity
import ru.samitin.translater.model.data.state.AppState

abstract class BaseActivity<T: AppState> : AppCompatActivity() {
    // В каждой Активити будет своя ViewModel, которая наследуется от BaseViewModel
    abstract val model:BaseViewModel<T>


    abstract fun renderData(appState: AppState)


}