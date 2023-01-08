package com.sogya.domain.repository


import androidx.lifecycle.LiveData
import com.sogya.domain.models.ServerStateDomain
import com.sogya.domain.models.StateDomain
import com.sogya.domain.models.StateGroupDomain

interface LocalDataBaseRepository {

    fun getAllStates(serverUri: String): LiveData<List<StateDomain>>

    fun getStateById(entityId: String): LiveData<List<StateDomain>>

    fun insertState(states: List<StateDomain>)

    fun insertOneState(states: StateDomain)

    fun deleteState(stateId: String)

    fun getAllGroupsByOwner(ownerId: String): LiveData<List<StateGroupDomain>>

    fun insertGroup(stateGroupDomain: StateGroupDomain)

    fun deleteGroup(stateGroupId: Int)

    fun getAllServers(): LiveData<List<ServerStateDomain>>

    fun getServerById(serverUri: String): ServerStateDomain

    fun insertServer(serverState: ServerStateDomain)

    fun deleteServer(serverState: ServerStateDomain)

    fun updateServer(serverState: ServerStateDomain)

    fun getAllByGroup(groupId: Int): LiveData<List<StateDomain>>
}