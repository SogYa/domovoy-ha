package ru.sogya.projects.domovoy.models.mappers

import com.sogya.domain.models.StateDomain
import ru.sogya.projects.domovoy.models.StatePresentation

class ListStateMapper(
    private val listState: List<StateDomain>
) {
    fun map(): List<StatePresentation> {
        val list = arrayListOf<StatePresentation>()
        listState.forEach {
            list.add(StateMapper(it).map())
        }
        return list
    }
}