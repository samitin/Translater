package ru.samitin.translater.view.main.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.samitin.translater.databinding.ActivityMainRecyclerviewItemBinding
import ru.samitin.model.DataModel
import ru.samitin.translater.utils.convertMeaningsToString

// Передаём в адаптер слушатель нажатия на список
class MainAdapter(
    private var onListItemClickListener: OnListItemClickListener,
) :RecyclerView.Adapter<MainAdapter.RecyclerViewViewHolder>(){
    private var data: List<DataModel> = arrayListOf()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<DataModel>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewViewHolder {
        return RecyclerViewViewHolder(
            ActivityMainRecyclerviewItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: RecyclerViewViewHolder, position: Int) {
        holder.bind(dataModel = data[position])
    }

    inner class RecyclerViewViewHolder(val binding: ActivityMainRecyclerviewItemBinding):ViewHolder(binding.root) {

        fun bind(dataModel: DataModel){
            if (layoutPosition != RecyclerView.NO_POSITION){
                binding.apply {
                    headerTextviewRecyclerItem.text = dataModel.text
                    descriptionTextviewRecyclerItem.text = convertMeaningsToString(dataModel.meanings!!)
                    // Вешаем слушатель
                    itemView.setOnClickListener { openInNewWindow(dataModel) }
                }
            }
        }
    }

    // Передаём событие в MainActivity
    private fun openInNewWindow(listItemData: DataModel){
        onListItemClickListener.onItemClick(dataModel = listItemData)
    }

    interface OnListItemClickListener{
        fun onItemClick(dataModel: DataModel)
    }


}