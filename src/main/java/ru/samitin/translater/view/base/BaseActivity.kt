package ru.samitin.translater.view.base

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import ru.samitin.translater.R
import ru.samitin.translater.databinding.LoadingLayoutBinding
import ru.samitin.translater.interactor.Interactor
import ru.samitin.translater.model.data.DataModel
import ru.samitin.translater.model.data.state.AppState
import ru.samitin.translater.utils.network.isOnline
import ru.samitin.translater.utils.ui.AlertDialogFragment
import ru.samitin.translater.view.base.BaseViewModel
private const val DIALOG_FRAGMENT_TAG = "74a54328-5d62-46bf-ab6b-cbf5d8c79522"
abstract class BaseActivity<T : AppState, I : Interactor<T>> : AppCompatActivity() {
    private lateinit var binding: LoadingLayoutBinding
    abstract val model: BaseViewModel<T>
    protected var isNetworkAvailable: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        isNetworkAvailable = isOnline(applicationContext)
    }
    override fun onResume() {
        super.onResume()
        binding = LoadingLayoutBinding.inflate(layoutInflater)
        isNetworkAvailable = isOnline(applicationContext)
        if (!isNetworkAvailable && isDialogNull()) {
            showNoInternetConnectionDialog()
        }
    }
    protected fun renderData(appState: T) {
        when (appState) {
            is AppState.Success -> {
                showViewWorking()
                appState.data?.let {
                    if (it.isEmpty()) {
                        showAlertDialog(
                            getString(R.string.dialog_tittle_sorry),
                            getString(R.string.empty_server_response_on_success)
                        )
                    } else {
                        setDataToAdapter(it)
                    }
                }
            }
            is AppState.Loading -> {
                showViewLoading()
                if (appState.progress != null) {
                    binding.progressBarHorizontal.visibility = View.VISIBLE
                    binding.progressBarRound.visibility = View.GONE
                    binding.progressBarHorizontal.progress = appState.progress
                } else {
                    binding.progressBarHorizontal.visibility = View.GONE
                    binding.progressBarRound.visibility = View.VISIBLE
                }
            }
            is AppState.Error -> {
                showViewWorking()
                showAlertDialog(getString(R.string.error_stub),
                    appState.error.message)
            }
        }
    }
    protected fun showNoInternetConnectionDialog() {
        showAlertDialog(
            getString(R.string.dialog_title_device_is_offline),
            getString(R.string.dialog_message_device_is_offline)
        )
    }
    protected fun showAlertDialog(title: String?, message: String?) {
        AlertDialogFragment.newInstance(title, message)
            .show(supportFragmentManager, DIALOG_FRAGMENT_TAG)
    }
    private fun showViewWorking() {
        binding.loadingFrameLayout.visibility = View.GONE
    }
    private fun showViewLoading() {
        binding.loadingFrameLayout.visibility = View.VISIBLE
    }
    private fun isDialogNull(): Boolean {
        return supportFragmentManager.findFragmentByTag(DIALOG_FRAGMENT_TAG) == null
    }
    // Объявим абстрактный метод и будем вызывать его в renderData, когда данные
// будут готовы для отображения
    abstract fun setDataToAdapter(data: List<DataModel>)
}