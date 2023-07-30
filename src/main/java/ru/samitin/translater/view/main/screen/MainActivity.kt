package ru.samitin.translater.view.main.screen

import android.os.Bundle
import android.view.View
import android.view.View.*
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel

import ru.samitin.translater.R
import ru.samitin.translater.view.base.BaseActivity
import ru.samitin.translater.databinding.ActivityMainBinding
import ru.samitin.translater.model.data.DataModel
import ru.samitin.translater.model.data.state.AppState
import ru.samitin.translater.utils.network.isOnline
import ru.samitin.translater.view.main.adapter.MainAdapter
import ru.samitin.translater.view.main.interactor.MainInteractor
import ru.samitin.translater.view.main.screen.search.SearchDialogFragment
import ru.samitin.translater.view.main.viewModel.MainViewModel


// Контракта уже нет
class MainActivity : BaseActivity<AppState, MainInteractor>() {


    private lateinit var binding: ActivityMainBinding
    override lateinit var model: MainViewModel
    private val adapter: MainAdapter by lazy { MainAdapter(onListItemClickListener) }

    private val onListItemClickListener: MainAdapter.OnListItemClickListener =
        object : MainAdapter.OnListItemClickListener {
            override fun onItemClick(data: DataModel) {
                Toast.makeText(this@MainActivity, data.text, Toast.LENGTH_SHORT).show()
            }
        }
    private val onSearchClickListener: SearchDialogFragment.OnSearchListener =
        object : SearchDialogFragment.OnSearchListener {
            override fun onClick(searchWord: String) {
                isNetworkAvailable = isOnline(applicationContext)
                if (isNetworkAvailable) {
                    model.getData(searchWord, isNetworkAvailable)
                } else {
                    showNoInternetConnectionDialog()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initVieModel()
        initViews()
    }
    private fun initVieModel(){
        // Убедимся, что модель инициализируется раньше View
        if (binding.mainActivityRecyclerview.adapter != null)
            throw IllegalStateException("The ViewModel should be initialised first")

        // Теперь ViewModel инициализируется через функцию by viewModel()
        // Это функция, предоставляемая Koin из коробки через зависимость
        // import org.koin.androidx.viewmodel.ext.android.viewModel
        val viewModel:MainViewModel by viewModel()
        model = viewModel
        model.subscribe().observe(this@MainActivity){renderData(it)}
    }
    private fun initViews(){
        val searchDialogFragment = SearchDialogFragment.newInstance()
        searchDialogFragment.setOnSearchClickListener(onSearchClickListener)
        supportFragmentManager.beginTransaction()
            .add(binding.searchContainer.id,searchDialogFragment)
            .commit()
        binding.mainActivityRecyclerview.layoutManager = LinearLayoutManager(applicationContext)
        binding.mainActivityRecyclerview.adapter = adapter
    }


    override fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                showViewWorking()
                val data = appState.data
                if (data.isNullOrEmpty()) {
                    showAlertDialog(
                        getString(R.string.dialog_tittle_sorry),
                        getString(R.string.empty_server_response_on_success)
                    )
                } else {
                    adapter.setData(data)
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
                showViewWorking()
                showAlertDialog(getString(R.string.error_stub), appState.error.message)
            }
        }
    }

    private fun showViewWorking() {
        binding.loadingFrameLayout.visibility = GONE
    }

    private fun showViewLoading() {
        binding.loadingFrameLayout.visibility = VISIBLE
    }

    companion object {
        private const val BOTTOM_SHEET_FRAGMENT_DIALOG_TAG =
            "74a54328-5d62-46bf-ab6b-cbf5fgt0-092395"
    }
}
