package com.sogya.domain.repository


import com.sogya.domain.models.ServerStateDomain
import com.sogya.domain.models.StateDomain
import com.sogya.domain.models.StateGroupDomain
import com.sogya.domain.models.ZoneDomain
import kotlinx.coroutines.flow.Flow

interface LocalDataBaseRepository {

    fun getAllStates(serverUri: String): Flow<List<StateDomain>>

    fun getStateById(entityId: String): StateDomain

    fun getStateByIdFlow(entityId: String): Flow<StateDomain>

    fun insertState(states: List<StateDomain>)

    fun insertOneState(states: StateDomain)

    fun deleteState(stateId: String)

    fun isStateInDB(stateId: String): Boolean

    fun updateState(stateDomain: StateDomain)

    fun updateStates(stateList: List<StateDomain>)

    suspend fun getAllGroupsByOwner(ownerId: String): Flow<List<StateGroupDomain>>

    fun insertGroup(stateGroupDomain: StateGroupDomain)

    fun deleteGroup(stateGroupId: Int)

    fun getAllServers(): Flow<List<ServerStateDomain>>

    fun getServerById(serverUri: String): ServerStateDomain

    fun insertServer(serverState: ServerStateDomain)

    fun deleteServer(serverId: String)

    fun updateServer(serverState: ServerStateDomain)

    fun getAllByGroup(groupId: Int): Flow<List<StateDomain>>
    fun getAllZones(): Flow<List<ZoneDomain>>

    fun insertZone(zoneDomain: ZoneDomain)

    fun deleteZone(zoneDomain: ZoneDomain)
}