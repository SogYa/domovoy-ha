package ru.sogya.projects.domovoy.screens.settings

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.sogya.projects.domovoy.R
import ru.sogya.projects.domovoy.models.Settings

class SettingsAdapter(
    private val onSettingsClickListenner: OnSettingsClickListenner
) : RecyclerView.Adapter<SettingsAdapter.ViewHolder>() {
    private var settings = ArrayList<Settings>()


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val labelTextView: TextView = itemView.findViewById(R.id.settings_label)
        val descTextView: TextView = itemView.findViewById(R.id.settings_desc)
        val iconImageView: ImageView = itemView.findViewById(R.id.settings_icon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.settings_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val settings: Settings = settings[position]
        holder.labelTextView.text = settings.label
        holder.descTextView.text = settings.desc
        holder.iconImageView.setImageResource(settings.resourceId)
        holder.itemView.setOnClickListener {
            onSettingsClickListenner.onClick(settings)
        }
    }

    fun updateSettingsList(buildingArrayList: List<Settings>) {
        this.settings.clear()
        notifyItemChanged(1)
        this.settings.addAll(buildingArrayList)
        notifyItemRangeChanged(0, settings.size)
    }

    override fun getItemCount(): Int {
        return settings.size
    }

    interface OnSettingsClickListenner {
        fun onClick(settings: Settings)
    }
}
