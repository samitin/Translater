package ru.samitin.translater.view.main.screen

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.AnticipateInterpolator
import androidx.annotation.RequiresApi
import androidx.core.animation.doOnEnd
import androidx.lifecycle.Observer
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.samitin.core.BaseActivity
import ru.samitin.history.HistoryActivity
import ru.samitin.model.entity.DataModel
import ru.samitin.translater.R
import ru.samitin.translater.databinding.ActivityMainBinding
import ru.samitin.model.state.AppState
import ru.samitin.translater.utils.convertMeaningsToSingleString
import ru.samitin.translater.view.description.DescriptionActivity
import ru.samitin.translater.view.main.adapter.MainAdapter
import ru.samitin.translater.view.main.interactor.MainInteractor
import ru.samitin.translater.view.main.screen.search.SearchDialogFragment
import ru.samitin.translater.view.main.viewModel.MainViewModel
import ru.samitin.utils.ui.viewById

private const val SLIDE_LEFT_DURATION = 2000L
// Контракта уже нет
class MainActivity : BaseActivity<AppState, MainInteractor>() {
    //Объявляем переменные на уровне класса
    private val mainActivityRecyclerview by
    viewById<RecyclerView>(R.id.main_activity_recyclerview)

    private lateinit var binding: ActivityMainBinding
    override lateinit var model: MainViewModel
    private val adapter: MainAdapter by lazy { MainAdapter(onListItemClickListener) }

    private val onListItemClickListener: MainAdapter.OnListItemClickListener =
        object : MainAdapter.OnListItemClickListener {
            override fun onItemClick(data: DataModel) {
                startActivity(
                    DescriptionActivity.getIntent(
                        this@MainActivity,
                        data.text!!,
                        convertMeaningsToSingleString(data.meanings!!),
                        data.meanings!![0].imageUrl
                    )
                )
            }
        }
    private val onSearchClickListener: SearchDialogFragment.OnSearchListener =
        object : SearchDialogFragment.OnSearchListener {
            override fun onClick(searchWord: String) {
                //isNetworkAvailable = isOnline(applicationContext)
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
        setDefaultSplashScreen()
        initVieModel()
        initViews()
    }
    private fun setDefaultSplashScreen(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            setSplashScreenHideAnimation()
        setSplashScreenDuration()
    }
    @RequiresApi(31)
    private fun setSplashScreenHideAnimation(){
        splashScreen.setOnExitAnimationListener { splashScreenView ->
            val slideLeft = ObjectAnimator.ofFloat(
                splashScreenView,
                View.TRANSLATION_X,
                0f,
                -splashScreenView.height.toFloat()
            )
            slideLeft.interpolator = AnticipateInterpolator()
            slideLeft.duration = SLIDE_LEFT_DURATION

            slideLeft.doOnEnd { splashScreenView.remove() }
            slideLeft.start()
        }
    }

    private fun setSplashScreenDuration(){
        var isHideSplashScreen = false

        object :CountDownTimer(2000,1000){
            override fun onTick(p0: Long) {}
            override fun onFinish() {
                isHideSplashScreen =true
            }
        }.start()

        val content: View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener (
            object : ViewTreeObserver.OnPreDrawListener{
                override fun onPreDraw(): Boolean {
                    return if (isHideSplashScreen){
                        content.viewTreeObserver.removeOnPreDrawListener(this)
                        true
                    }else{
                        false
                    }
                }
            }
        )
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
        model.subscribe().observe(this@MainActivity, Observer<AppState> { renderData(it) })
    }
    private fun initViews(){
        val searchDialogFragment = SearchDialogFragment.newInstance()
        searchDialogFragment.setOnSearchClickListener(onSearchClickListener)
        supportFragmentManager.beginTransaction()
            .add(binding.searchContainer.id,searchDialogFragment)
            .commit()
        mainActivityRecyclerview.layoutManager = LinearLayoutManager(applicationContext)
        mainActivityRecyclerview.adapter = adapter
    }


    override fun setDataToAdapter(data: List<DataModel>) {
        adapter.setData(data)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.history_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.menu_history -> {
                startActivity(Intent(this, HistoryActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        private const val BOTTOM_SHEET_FRAGMENT_DIALOG_TAG =
            "74a54328-5d62-46bf-ab6b-cbf5fgt0-092395"
    }
}
