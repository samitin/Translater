package ru.samitin.translater.view.main.screen

import android.os.Bundle
import android.view.View.*
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import ru.samitin.translater.R
import ru.samitin.translater.view.base.BaseActivity
import ru.samitin.translater.databinding.ActivityMainBinding
import ru.samitin.translater.model.data.DataModel
import ru.samitin.translater.model.data.state.AppState
import ru.samitin.translater.view.base.BaseViewModel
import ru.samitin.translater.view.main.adapter.MainAdapter
import ru.samitin.translater.view.main.screen.search.SearchDialogFragment
import ru.samitin.translater.view.main.viewModel.MainViewModel

// Контракта уже нет
class MainActivity : BaseActivity<AppState>() {
    // Создаём модель
    override val model: BaseViewModel<AppState> by lazy {
        ViewModelProvider.NewInstanceFactory().create(MainViewModel::class.java)
    }
    // Паттерн Observer в действии. Именно с его помощью мы подписываемся на
    // изменения в LiveData
    private val observer=Observer<AppState>{renderData(it)}
    private lateinit var binding: ActivityMainBinding
    private var adapter: MainAdapter? = null
    private val onListItemClickListener: MainAdapter.OnListItemClickListener =
        object : MainAdapter.OnListItemClickListener {
            override fun onItemClick(data: DataModel) {
                Toast.makeText(this@MainActivity, data.text,
                    Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.searchFab.setOnClickListener {
            val searchDialogFragment = SearchDialogFragment.newInstance()
            searchDialogFragment.setOnSearchClickListener(object :
                    SearchDialogFragment.OnSearchClickListener {
                override fun onClick(searchWord: String) {
                // Обратите внимание на этот ключевой момент. У ViewModel
                // мы получаем LiveData через метод getData и подписываемся
                // на изменения, передавая туда observer
                    model.getData(searchWord,true).observe(this@MainActivity,observer)
                }
            })
            searchDialogFragment.show(supportFragmentManager, BOTTOM_SHEET_FRAGMENT_DIALOG_TAG)
        }
    }
    override fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                val dataModel = appState.data
                if (dataModel == null || dataModel.isEmpty()) {
                    showErrorScreen(getString(R.string.empty_server_response_on_success))
                } else {
                    showViewSuccess()
                    if (adapter == null) {
                        binding.mainActivityRecyclerview.layoutManager =
                            LinearLayoutManager(applicationContext)
                        binding.mainActivityRecyclerview.adapter =
                            MainAdapter(onListItemClickListener, dataModel)
                    } else {
                        adapter!!.setData(dataModel)
                    }
                }
            }
            is AppState.Loading -> {
                showViewLoading()
                if (appState.progress != null) {
                    binding.progressBarHorizontal.visibility = VISIBLE
                    binding.progressBarRound.visibility = GONE
                    binding.progressBarHorizontal.progress = appState.progress
                } else {
                    binding.progressBarHorizontal.visibility = GONE
                    binding.progressBarRound.visibility = VISIBLE
                }
            }
            is AppState.Error -> {
                showErrorScreen(appState.error.message)
            }
        }
    }
    // Удаляем ненужные вспомогательные методы типа createPresenter. Всё остальное
    // - без изменений, за исключением одной детали:
    private fun showErrorScreen(error: String?) {
        showViewError()
        binding.errorTextview.text = error ?: getString(R.string.undefined_error)
        binding.reloadButton.setOnClickListener {
        // В случае ошибки мы повторно запрашиваем данные и подписываемся
        // на изменения
            model.getData("hi",true).observe(this,observer)
        }
    }
    private fun showViewSuccess() {
        binding.successLinearLayout.visibility = VISIBLE
        binding.loadingFrameLayout.visibility = GONE
        binding.errorLinearLayout.visibility = GONE
    }
    private fun showViewLoading() {
        binding.successLinearLayout.visibility = GONE
        binding.loadingFrameLayout.visibility = VISIBLE
        binding.errorLinearLayout.visibility = GONE
    }
    private fun showViewError() {
        binding.successLinearLayout.visibility = GONE
        binding.loadingFrameLayout.visibility = GONE
        binding.errorLinearLayout.visibility = VISIBLE
    }
    companion object {
        private const val BOTTOM_SHEET_FRAGMENT_DIALOG_TAG =
            "74a54328-5d62-46bf-ab6b-cbf5fgt0-092395"
    }
}