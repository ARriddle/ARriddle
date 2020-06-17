package fr.ec.arridle.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import fr.ec.arridle.databinding.ItemKeypointBinding
import fr.ec.arridle.network.KeypointProperty

class KeypointAdapter(private val onClickListener: OnClickListener) :
    ListAdapter<KeypointProperty, KeypointAdapter.KeypointPropertyViewHolder>(DiffCallback) {

    /**
     * The KeypointPropertyViewHolder constructor takes the binding variable from the associated
     * GridViewItem, which nicely gives it access to the full [KeypointProperty] information.
     */
    class KeypointPropertyViewHolder(private var binding: ItemKeypointBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(keypointProperty: KeypointProperty) {
            binding.property = keypointProperty
            // This is important, because it forces the data binding to execute immediately,
            // which allows the RecyclerView to make the correct view size measurements
            binding.executePendingBindings()
        }
    }

    /**
     * Allows the RecyclerView to determine which items have changed when the [List] of [KeypointProperty]
     * has been updated.
     */
    companion object DiffCallback : DiffUtil.ItemCallback<KeypointProperty>() {
        override fun areItemsTheSame(oldItem: KeypointProperty, newItem: KeypointProperty): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: KeypointProperty, newItem: KeypointProperty): Boolean {
            return oldItem.id == newItem.id
        }
    }

    /**
     * Create new [RecyclerView] item views (invoked by the layout manager)
     */
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): KeypointPropertyViewHolder {
        return KeypointPropertyViewHolder(ItemKeypointBinding.inflate(LayoutInflater.from(parent.context)))
    }

    /**
     * Replaces the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: KeypointPropertyViewHolder, position: Int) {
        val keypointProperty = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(keypointProperty)
        }
        holder.bind(keypointProperty)
    }


    /**
     * Custom listener that handles clicks on [RecyclerView] items.  Passes the [keypointId]
     * associated with the current item to the [onClick] function.
     * @param clickListener lambda that will be called with the current [KeypointId]
     */
    class OnClickListener(val clickListener: (keypointProperty: KeypointProperty) -> Unit) {
        fun onClick(keypointProperty: KeypointProperty) {
            clickListener(keypointProperty)
        }
    }

}
