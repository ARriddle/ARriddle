package fr.ec.arridle.adapters

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import fr.ec.arridle.network.GameProperty
import fr.ec.arridle.network.KeypointProperty

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<GameProperty>?) {
    val adapter = recyclerView.adapter as GameAdapter
    adapter.submitList(data)
}

@BindingAdapter("listKeypoints")
fun bindRecyclerViewKeypoints(recyclerView: RecyclerView, data: List<KeypointProperty>?) {
    val adapter = recyclerView.adapter as KeypointAdapter
    adapter.submitList(data)
}
