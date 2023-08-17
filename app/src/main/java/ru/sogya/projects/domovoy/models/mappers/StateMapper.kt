package ru.sogya.projects.domovoy.models.mappers

import com.sogya.domain.models.StateDomain
import ru.sogya.projects.domovoy.models.StatePresentation

class StateMapper(
    private val stateDomain: StateDomain
) {
    fun map(): StatePresentation = StatePresentation(
        stateDomain.entityId,
        stateDomain.state,
        stateDomain.lastUpdated,
        stateDomain.lastChanged,
        AttributeMapper(stateDomain.attributes).map(),
        stateDomain.ownerId,
        stateDomain.groupId
    )
}