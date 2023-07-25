package com.example.newfilms.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import com.example.newfilms.R
import com.example.newfilms.databinding.ItemLayoutBinding
import com.example.newfilms.api.response.FilmResponseList
import com.example.newfilms.activities.FilmDetailsActivity
import com.example.newfilms.utils.Constants.POSTER_BASE_URL

class FilmsAdapter : RecyclerView.Adapter<FilmsAdapter.ViewHolder>() {

    private lateinit var binding: ItemLayoutBinding
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = ItemLayoutBinding.inflate(inflater, parent, false)
        context = parent.context
        return ViewHolder()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class ViewHolder : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(item: FilmResponseList.Result) {
            binding.apply {
                title.text = item.title
                descCalendar.text = item.releaseDate
                rateDescription.text=item.voteAverage.toString()
                val moviePosterURL = POSTER_BASE_URL + item.posterPath
                imageView.load(moviePosterURL){
                    (true)
                    placeholder(R.drawable.poster_placeholder)
                    scale(Scale.FILL)
                }
                descLang.text=item.originalLanguage

                root.setOnClickListener {
                    val intent = Intent(context, FilmDetailsActivity::class.java)
                    intent.putExtra("id", item.id)
                    context.startActivity(intent)
                }
            }
        }
    }
    private val differCallback = object : DiffUtil.ItemCallback<FilmResponseList.Result>() {
        override fun areItemsTheSame(oldItem: FilmResponseList.Result, newItem: FilmResponseList.Result): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: FilmResponseList.Result, newItem: FilmResponseList.Result): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, differCallback)
}