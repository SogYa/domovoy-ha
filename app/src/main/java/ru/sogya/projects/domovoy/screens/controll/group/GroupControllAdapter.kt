package ru.sogya.projects.domovoy.screens.controll.group

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sogya.domain.utils.Constants
import com.sogya.domain.models.StateGroupDomain
import ru.sogya.projects.domovoy.R

class GroupControllAdapter(
    private val onGroupClickListener: OnGroupClickListener,
) : RecyclerView.Adapter<GroupControllAdapter.ViewHolder>() {
    private var groups = ArrayList<StateGroupDomain>()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val groupLabel: TextView = itemView.findViewById(R.id.group_label)
        val groupDesc: TextView = itemView.findViewById(R.id.group_desc)
        val groupCounter: TextView = itemView.findViewById(R.id.textViewCounter)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.group_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val group = groups[position]
        val currentPosition = position + 1
        holder.groupLabel.text = group.groupTag
        holder.groupCounter.text = currentPosition.toString()
        val groupDesc = group.groupDesc
        if (groupDesc != "") {
            holder.groupDesc.text = groupDesc
        } else {
            holder.groupDesc.text = holder.itemView.context.getString(R.string.group_empty_desc)
        }
        holder.itemView.setOnClickListener {
            onGroupClickListener.onClick(group)
        }
        holder.itemView.setOnLongClickListener {
            onGroupClickListener.onLongClick(group)
            return@setOnLongClickListener true
        }

    }

    fun updateGroupsList(groupsList: List<StateGroupDomain>) {
        groups.clear()
        notifyItemChanged(1)
        groups.add(
            StateGroupDomain(
                Constants.DEFAULT_GROUP_ID,
                "All states", "All states",
                "A shared dashboard with all the states added to the app"
            )
        )
        groups.addAll(groupsList)

        notifyItemRangeChanged(0, groups.size)
    }

    override fun getItemCount(): Int {
        return groups.size
    }

    interface OnGroupClickListener {
        fun onClick(stateGroupDomain: StateGroupDomain)
        fun onLongClick(stateGroupDomain: StateGroupDomain)
    }
}