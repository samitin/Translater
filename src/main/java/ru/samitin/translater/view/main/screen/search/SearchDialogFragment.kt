package ru.samitin.translater.view.main.screen.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import ru.samitin.translater.databinding.SearchDialogFragmentBinding

class SearchDialogFragment : BottomSheetDialogFragment() {
    private val job: Job = Job()
    private var _binding: SearchDialogFragmentBinding? = null
    private val binding get() = _binding!!
    private var onSearchListener: OnSearchListener? = null

    internal fun setOnSearchClickListener(listener: OnSearchListener) {
        onSearchListener = listener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = SearchDialogFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpSearchStateFlow()
        addOnClearClickListener()
    }

    private fun setUpSearchStateFlow() {
        CoroutineScope(Dispatchers.Main + job).launch {
            binding.searchEditText.getQueryTextChangeStateFlow()
            .debounce(500)
            //фильтрует пустые строки:
            .filter { query ->
                return@filter query.isNotEmpty()
            }
            //позволяет избегать дублирующие запросы:
            .distinctUntilChanged()
            //возвращает в поток только самый последний запрос и игнорирует более ранние:
            .flatMapLatest { query ->
                flow<String> { emit(query) }
            }
            .collect { result -> onSearchListener?.onClick(result) }
        }

    }


    override fun onDestroyView() {
        onSearchListener = null
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
    private fun addOnClearClickListener() {
        binding.clearTextImageview.setOnClickListener {
            binding.searchEditText.setText("")
        }
    }
    interface OnSearchListener {
        fun onClick(searchWord: String)
    }
    companion object {
        fun newInstance(): SearchDialogFragment {
            return SearchDialogFragment()
        }
    }

    fun TextInputEditText.getQueryTextChangeStateFlow():StateFlow<String>{
        val query = MutableStateFlow("")
        addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (text != null &&
                    !text.toString().isEmpty()) {
                    binding.clearTextImageview.visibility = View.VISIBLE
                    query.value =s.toString()
                } else {
                    binding.clearTextImageview.visibility = View.GONE
                }
            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {}
        })
          return query
    }
}