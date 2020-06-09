package fr.ec.arridle.adapters

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import fr.ec.arridle.network.GameProperty
import fr.ec.arridle.network.UserProperty

@BindingAdapter("listGame")
fun bindRecyclerViewGame(recyclerView: RecyclerView, data: List<GameProperty>?) {
    val adapter = recyclerView.adapter as GameAdapter
    adapter.submitList(data)
}

@BindingAdapter("listLeaderboard")
fun bindRecyclerViewLeaderboard(recyclerView: RecyclerView, data: List<UserProperty>?) {
    val adapter = recyclerView.adapter as UserAdapter
    adapter.submitList(data)
}