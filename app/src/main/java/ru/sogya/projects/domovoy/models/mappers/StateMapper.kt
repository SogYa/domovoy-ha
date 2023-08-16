package ru.sogya.projects.domovoy.models.mappers

import com.sogya.data.mappers.state.attributes.AttributeDomainMapper
import com.sogya.domain.models.StateDomain
import ru.sogya.projects.domovoy.models.StatePresenation

class StateMapper(
    private val stateDomain: StateDomain
) {
    fun map():StatePresenation = StatePresenation(
        stateDomain.entityId,
        stateDomain.state,
        stateDomain.lastUpdated,
        stateDomain.lastChanged,
        AttributeMapper(stateDomain.attributes).map(),
        stateDomain.ownerId,
        stateDomain.groupId
    )
}