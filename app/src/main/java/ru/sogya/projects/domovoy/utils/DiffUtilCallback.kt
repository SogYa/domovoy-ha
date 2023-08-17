package ru.sogya.projects.domovoy.utils

import androidx.recyclerview.widget.DiffUtil
import ru.sogya.projects.domovoy.models.StatePresentation

class DiffUtilCallback : DiffUtil.ItemCallback<StatePresentation>() {
    override fun areItemsTheSame(
        oldItem: StatePresentation,
        newItem: StatePresentation
    ): Boolean {
        return oldItem.entityId == newItem.entityId
    }

    override fun areContentsTheSame(
        oldItem: StatePresentation,
        newItem: StatePresentation
    ): Boolean {
        return oldItem == newItem
    }
}