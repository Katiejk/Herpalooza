package com.kkarabet.herpalooza.database


import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface HerpDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(herp:Herp)

    @Query("DELETE FROM herps_table")
    fun deleteAll()

    @Update
    fun updateHerp(herp:Herp)

    @Delete
    fun deleteHerp(herp:Herp)

    @Query("SELECT * FROM herps_table LIMIT 1")
    fun getHerp(): Array<Herp>

    @Query("SELECT * FROM herps_table ORDER BY name ASC")
    fun getAllHerpsName(): LiveData<List<Herp>>

    @Query("SELECT * FROM herps_table ORDER BY type ASC")
    fun getAllHerpsType(): LiveData<List<Herp>>

    @Query("SELECT * FROM herps_table WHERE found = 0 ORDER BY name ASC")
    fun getAllHerpsNameFound(): LiveData<List<Herp>>

    @Query("SELECT * FROM herps_table WHERE found = 0 ORDER BY type ASC")
    fun getAllHerpsTypeFound(): LiveData<List<Herp>>

    @Query("SELECT * FROM herps_table WHERE type = :number ORDER BY name ASC")
    fun getAllHerpsNameType(number:Int): LiveData<List<Herp>>

    @Query("SELECT * FROM herps_table WHERE found = 0 AND type = :number ORDER BY name ASC")
    fun getAllHerpsNameFoundType(number:Int): LiveData<List<Herp>>

    @Query("SELECT * FROM herps_table WHERE type = :number ORDER BY type ASC")
    fun getAllHerpsTypeType(number:Int): LiveData<List<Herp>>

    @Query("SELECT * FROM herps_table WHERE found = 0 AND type = :number ORDER BY type ASC")
    fun getAllHerpsTypeFoundType(number:Int): LiveData<List<Herp>>

}