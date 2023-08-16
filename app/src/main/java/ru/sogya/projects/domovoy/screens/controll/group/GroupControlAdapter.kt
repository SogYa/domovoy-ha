package ru.sogya.projects.domovoy.screens.controll.group

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sogya.domain.models.StateGroupDomain
import ru.sogya.projects.domovoy.R

class GroupControlAdapter: RecyclerView.Adapter<GroupControlAdapter.ViewHolder>() {
    private var groups = ArrayList<StateGroupDomain>()
    private var onGroupClickListener: OnGroupClickListener? = null

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val groupLabel: TextView = itemView.findViewById(R.id.group_label)
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
        holder.itemView.setOnLongClickListener {
            onGroupClickListener?.onLongClick(group)
            return@setOnLongClickListener true
        }

    }

    fun updateGroupList(groupsList: List<StateGroupDomain>) {
        groups.clear()
        notifyItemChanged(1)
        groups.addAll(groupsList)
        notifyItemRangeChanged(0, groups.size)
    }

    override fun getItemCount(): Int {
        return groups.size
    }

    interface OnGroupClickListener {
        fun onLongClick(stateGroupDomain: StateGroupDomain)
    }
    fun setOnClickListener(onClickListener: OnGroupClickListener) {
        this.onGroupClickListener = onClickListener
    }
}