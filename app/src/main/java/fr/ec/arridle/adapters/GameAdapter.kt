package fr.ec.arridle.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import fr.ec.arridle.databinding.ItemGameBinding
import fr.ec.arridle.network.GameProperty

class GameAdapter() :
    ListAdapter<GameProperty,
            GameAdapter.GamePropertyViewHolder>(DiffCallback) {

    /**
     * The GamePropertyViewHolder constructor takes the binding variable from the associated
     * GridViewItem, which nicely gives it access to the full [GameProperty] information.
     */
    class GamePropertyViewHolder(private var binding: ItemGameBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(gameProperty: GameProperty) {
            binding.property = gameProperty
            // This is important, because it forces the data binding to execute immediately,
            // which allows the RecyclerView to make the correct view size measurements
            binding.executePendingBindings()
        }
    }

    /**
     * Allows the RecyclerView to determine which items have changed when the [List] of [GameProperty]
     * has been updated.
     */
    companion object DiffCallback : DiffUtil.ItemCallback<GameProperty>() {
        override fun areItemsTheSame(oldItem: GameProperty, newItem: GameProperty): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: GameProperty, newItem: GameProperty): Boolean {
            return oldItem.id == newItem.id
        }
    }

    /**
     * Create new [RecyclerView] item views (invoked by the layout manager)
     */
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GamePropertyViewHolder {
        return GamePropertyViewHolder(ItemGameBinding.inflate(LayoutInflater.from(parent.context)))
    }

    /**
     * Replaces the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: GamePropertyViewHolder, position: Int) {
        val gameProperty = getItem(position)
        /*holder.itemView.setOnClickListener {
            onClickListener.onClick(gameProperty)
        }*/
        holder.bind(gameProperty)
    }
}
