package ru.samitin.translater.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ru.samitin.translater.view.main.screen.MainActivity


@Module
abstract class ActivityModule {

    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity
}
