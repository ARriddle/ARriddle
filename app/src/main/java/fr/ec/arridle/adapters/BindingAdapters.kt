package fr.ec.arridle.adapters

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import fr.ec.arridle.network.GameProperty

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<GameProperty>?) {
    val adapter = recyclerView.adapter as GameAdapter
    adapter.submitList(data)
}