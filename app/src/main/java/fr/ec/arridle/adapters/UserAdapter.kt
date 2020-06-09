package fr.ec.arridle.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import fr.ec.arridle.databinding.ItemUserBinding
import fr.ec.arridle.network.UserProperty

class UserAdapter() :
    ListAdapter<UserProperty,
            UserAdapter.UserPropertyViewHolder>(DiffCallback) {

    /**
     * The UserPropertyViewHolder constructor takes the binding variable from the associated
     * GridViewItem, which nicely gives it access to the full [UserProperty] information.
     */
    class UserPropertyViewHolder(private var binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(userProperty: UserProperty) {
            binding.property = userProperty
            // This is important, because it forces the data binding to execute immediately,
            // which allows the RecyclerView to make the correct view size measurements
            binding.executePendingBindings()
        }
    }

    /**
     * Allows the RecyclerView to determine which items have changed when the [List] of [UserProperty]
     * has been updated.
     */
    companion object DiffCallback : DiffUtil.ItemCallback<UserProperty>() {
        override fun areItemsTheSame(oldItem: UserProperty, newItem: UserProperty): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: UserProperty, newItem: UserProperty): Boolean {
            return oldItem.id == newItem.id
        }
    }

    /**
     * Create new [RecyclerView] item views (invoked by the layout manager)
     */
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserPropertyViewHolder {
        return UserPropertyViewHolder(ItemUserBinding.inflate(LayoutInflater.from(parent.context)))
    }

    /**
     * Replaces the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: UserPropertyViewHolder, position: Int) {
        val userProperty = getItem(position)
        /*holder.itemView.setOnClickListener {
            onClickListener.onClick(userProperty)
        }*/
        holder.bind(userProperty)
    }
}
