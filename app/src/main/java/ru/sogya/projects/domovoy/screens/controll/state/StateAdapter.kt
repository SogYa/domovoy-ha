package ru.sogya.projects.domovoy.screens.controll.state

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sogya.domain.models.StateDomain
import ru.sogya.projects.domovoy.R
import java.util.Locale

@Suppress("UNCHECKED_CAST")
class StateAdapter : RecyclerView.Adapter<StateAdapter.ViewHolder>(), Filterable {
    private var states = ArrayList<StateDomain>()
    private val checkedSet = HashSet<StateDomain>()
    private var searchStatesList = states
    private val differ = AsyncListDiffer(this, DiffCallback())

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.textFriendlyName)
        val idTextView: TextView = itemView.findViewById(R.id.textId)
        val stateCountTextView: TextView = itemView.findViewById(R.id.textStateCount)
        val stateSelectedIcon: ImageView = itemView.findViewById(R.id.imageViewSelected)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.state_default_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return searchStatesList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val stateDomain: StateDomain = searchStatesList[position]
        if (checkedSet.contains(stateDomain)) {
            holder.stateSelectedIcon.visibility = View.VISIBLE
        } else {
            holder.stateSelectedIcon.visibility = View.GONE
        }
        holder.nameTextView.text = stateDomain.attributes!!.friendlyName
        holder.idTextView.text = stateDomain.entityId
        holder.stateCountTextView.text = position.toString()
        holder.itemView.setOnClickListener {
            if (checkedSet.contains(stateDomain)) {
                holder.stateSelectedIcon.visibility = View.GONE
                checkedSet.remove(stateDomain)
            } else {
                holder.stateSelectedIcon.visibility = View.VISIBLE
                checkedSet.add(stateDomain)
            }
        }
    }

    fun sendCheckedSet(): HashSet<StateDomain> = checkedSet
    fun clearCheckedSet() {
        checkedSet.clear()
    }

    fun updateStatesList(statesArrayList: List<StateDomain>) {
        states.clear()
        notifyItemRemoved(1)
        states.addAll(statesArrayList)
        notifyItemRangeChanged(0, states.size)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    searchStatesList = states
                    Log.d("EmptyList", searchStatesList.toString())
                } else {
                    val resultList = ArrayList<StateDomain>()
                    for (row in searchStatesList) {
                        if (row.entityId.lowercase(Locale.ROOT)
                                .contains(charSearch.lowercase(Locale.ROOT))
                        ) {
                            resultList.add(row)
                        }
                    }
                    searchStatesList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = searchStatesList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                searchStatesList = results?.values as ArrayList<StateDomain>
                notifyItemRangeChanged(0, searchStatesList.size)
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<StateDomain>() {
        override fun areItemsTheSame(oldItem: StateDomain, newItem: StateDomain): Boolean {
            return oldItem.entityId == newItem.entityId
        }

        override fun areContentsTheSame(oldItem: StateDomain, newItem: StateDomain): Boolean {
            return oldItem == newItem
        }

    }
}