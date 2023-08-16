package ru.sogya.projects.domovoy.models

import com.sogya.domain.models.AttributesDomain
import com.sogya.domain.models.StateDomain

data class StatePresenation(
    override val entityId: String,
    override val state: String,
    override val lastUpdated: String,
    override val lastChanged: String,
    override val attributes: AttributesDomain?,
    override var ownerId: String,
    override var groupId: Int
):StateDomain
