package com.sogya.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sogya.data.models.StateGroup
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupDao {

    @Query("SELECT * FROM stategroup WHERE ownerId IN(:ownerId)")
    fun getAllByOwner(ownerId: String): Flow<List<StateGroup>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGroup(stateGroup: StateGroup)

    @Query("DELETE FROM stategroup WHERE groupId IN (:groupId)")
    fun deleteGroup(groupId: Int)
}