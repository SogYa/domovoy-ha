package com.sogya.data.database.daos

import androidx.room.*
import com.sogya.data.models.ServerState
import kotlinx.coroutines.flow.Flow

@Dao
interface ServersDao {

    @Query("SELECT * FROM servers")
    fun getAll(): Flow<List<ServerState>>

    @Query("SELECT * FROM servers WHERE serverUri IN(:serverUri)")
    fun getById(serverUri: String): ServerState

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(serverState: ServerState)

    @Query("DELETE FROM servers WHERE serverUri IN (:serverId)")
    fun delete(serverId: String)

    @Update
    fun update(serverState: ServerState)
}