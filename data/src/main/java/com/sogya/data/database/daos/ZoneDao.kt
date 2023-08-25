package com.sogya.data.database.daos

import androidx.room.*
import com.sogya.data.models.ZoneData
import kotlinx.coroutines.flow.Flow

@Dao
interface ZoneDao {

    @Query("SELECT * FROM zones")
    fun getAllZones(): Flow<List<ZoneData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertZone(zoneData: ZoneData)

    @Delete
    fun deleteZone(zoneData: ZoneData)
}