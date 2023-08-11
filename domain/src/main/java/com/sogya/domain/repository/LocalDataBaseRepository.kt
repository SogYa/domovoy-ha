package com.sogya.domain.repository


import androidx.lifecycle.LiveData
import com.sogya.domain.models.ServerStateDomain
import com.sogya.domain.models.StateDomain
import com.sogya.domain.models.StateGroupDomain
import com.sogya.domain.models.ZoneDomain
import kotlinx.coroutines.flow.Flow

interface LocalDataBaseRepository {

    fun getAllStates(serverUri: String): LiveData<List<StateDomain>>

    fun getStateById(entityId: String): StateDomain

    fun getStateByIdLiveData(entityId: String): LiveData<StateDomain>

    fun insertState(states: List<StateDomain>)

    fun insertOneState(states: StateDomain)

    fun deleteState(stateId: String)

    fun isStateInDB(stateId: String): Boolean

    fun updateState(stateDomain: StateDomain)

    fun updateStates(stateList: List<StateDomain>)

    suspend fun getAllGroupsByOwner(ownerId: String): Flow<List<StateGroupDomain>>

    fun insertGroup(stateGroupDomain: StateGroupDomain)

    fun deleteGroup(stateGroupId: Int)

    fun getAllServers(): LiveData<List<ServerStateDomain>>

    fun getServerById(serverUri: String): ServerStateDomain

    fun insertServer(serverState: ServerStateDomain)

    fun deleteServer(serverId: String)

    fun updateServer(serverState: ServerStateDomain)

    fun getAllByGroup(groupId: Int): LiveData<List<StateDomain>>
    fun getAllZones(): LiveData<List<ZoneDomain>>

    fun insertZone(zoneDomain: ZoneDomain)

    fun deleteZone(zoneDomain: ZoneDomain)
}