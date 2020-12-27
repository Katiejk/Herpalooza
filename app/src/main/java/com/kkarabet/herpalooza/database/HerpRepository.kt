package com.kkarabet.herpalooza.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import java.util.concurrent.Executors

class HerpRepository private constructor(context: Context) {
    private val database: HerpDatabase = Room.databaseBuilder(
        context.applicationContext,
        HerpDatabase::class.java,
        "herps_database"
    ).build()

    private val herpDao = database.herpDao()
    private val executor = Executors.newSingleThreadExecutor()

    fun insert(herp:Herp){
        executor.execute {
            herpDao.insert(herp)
        }
    }

    fun deleteAll(){
        executor.execute{
            herpDao.deleteAll()
        }
    }

    fun updateHerp(herp:Herp){
        executor.execute{
            herpDao.updateHerp(herp)
        }
    }

    fun deleteHerp(herp:Herp){
        executor.execute{
            herpDao.deleteHerp(herp)
        }
    }

    fun getHerp(): Array<Herp> = herpDao.getHerp()

    fun getAllHerpsName(): LiveData<List<Herp>> = herpDao.getAllHerpsName()

    fun getAllHerpsType(): LiveData<List<Herp>> = herpDao.getAllHerpsType()

    fun getAllHerpsNameFound(): LiveData<List<Herp>> = herpDao.getAllHerpsNameFound()

    fun getAllHerpsTypeFound(): LiveData<List<Herp>> = herpDao.getAllHerpsTypeFound()

    fun getAllHerpsNameType(number:Int): LiveData<List<Herp>> = herpDao.getAllHerpsNameType(number)

    fun getAllHerpsNameFoundType(number:Int): LiveData<List<Herp>> = herpDao.getAllHerpsNameFoundType(number)

    fun getAllHerpsTypeType(number:Int): LiveData<List<Herp>> = herpDao.getAllHerpsTypeType(number)

    fun getAllHerpsTypeFoundType(number:Int): LiveData<List<Herp>> = herpDao.getAllHerpsTypeFoundType(number)


    companion object {
        private var INSTANCE: HerpRepository? = null
        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = HerpRepository(context)
            }
        }

        fun get():HerpRepository {
            return INSTANCE
                ?: throw IllegalStateException("HerpRepository must be initialized.")
        }
    }
}