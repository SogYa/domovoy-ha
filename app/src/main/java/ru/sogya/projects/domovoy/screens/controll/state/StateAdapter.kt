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
import androidx.recyclerview.widget.RecyclerView
import ru.sogya.projects.domovoy.R
import ru.sogya.projects.domovoy.models.StatePresentation
import ru.sogya.projects.domovoy.utils.DiffUtilCallback
import java.util.Locale

@Suppress("UNCHECKED_CAST")
class StateAdapter : RecyclerView.Adapter<StateAdapter.ViewHolder>(), Filterable {
    private var currentStateList = arrayListOf<StatePresentation>()
    private val checkedSet = HashSet<StatePresentation>()
    private var searchStatesList = currentStateList
    private val differ: AsyncListDiffer<StatePresentation> =
        AsyncListDiffer(this, DiffUtilCallback())

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
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val statePresentation: StatePresentation = differ.currentList[position]
        if (checkedSet.contains(statePresentation)) {
            holder.stateSelectedIcon.visibility = View.VISIBLE
        } else {
            holder.stateSelectedIcon.visibility = View.GONE
        }
        val itemCounter = currentStateList.indexOf(statePresentation) + 1
        holder.nameTextView.text = statePresentation.attributes!!.friendlyName
        holder.idTextView.text = statePresentation.entityId
        holder.stateCountTextView.text = itemCounter.toString()
        holder.itemView.setOnClickListener {
            if (checkedSet.contains(statePresentation)) {
                holder.stateSelectedIcon.visibility = View.GONE
                checkedSet.remove(statePresentation)
            } else {
                holder.stateSelectedIcon.visibility = View.VISIBLE
                checkedSet.add(statePresentation)
            }
        }
    }

    fun sendCheckedSet(): HashSet<StatePresentation> = checkedSet
    fun clearCheckedSet() {
        checkedSet.clear()
    }

    fun updateStatesList(newStateList: List<StatePresentation>) {
        currentStateList.clear()
        notifyItemChanged(1)
        currentStateList.addAll(newStateList)
        notifyItemRangeChanged(0, currentStateList.size)
        differ.submitList(newStateList)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty() || constraint == null) {
                    searchStatesList = currentStateList
                    Log.d("EmptyList", searchStatesList.toString())
                } else {
                    val resultList = ArrayList<StatePresentation>()
                    for (row in currentStateList) {
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
                val newSearchList = results?.values as ArrayList<StatePresentation>
                differ.submitList(newSearchList)
            }
        }
    }
}