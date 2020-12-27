package com.kkarabet.herpalooza.ui.main

import android.app.Application
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kkarabet.herpalooza.MainActivity.Companion.SHOW_FOUND
import com.kkarabet.herpalooza.MainActivity.Companion.SHOW_TYPE
import com.kkarabet.herpalooza.MainActivity.Companion.SORT_BY
import com.kkarabet.herpalooza.database.Herp
import com.kkarabet.herpalooza.database.HerpRepository

class MainViewModel (application: Application)  : AndroidViewModel(application) {

    init {
        HerpRepository.initialize(application)
    }

    private val settings: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(getApplication())
    }

    private val herpRepository = HerpRepository.get()


    fun insert(herp:Herp){
        herpRepository.insert(herp)
    }

    fun delete(herp: Herp){
        herpRepository.deleteHerp(herp)
    }

    fun deleteAll(){
        herpRepository.deleteAll()
    }

    private val herpMutable = MutableLiveData<Herp>()
    var currentProduct: LiveData<Herp> = herpMutable

    fun setCurrent(herp:Herp) {
        herpMutable.value = herp
    }

    private val foundMutable = MutableLiveData<Int>()
    var found: LiveData<Int> =foundMutable

    fun setFound(int:Int) {
        foundMutable.value = int
    }

    private val editingMutable = MutableLiveData<Int>()
    var editing: LiveData<Int> =editingMutable

    fun setEditing(int:Int) {
        editingMutable.value = int
    }

    fun upDateHerp(herp:Herp){
        herpRepository.updateHerp(herp)
    }

    fun getList():LiveData<List<Herp>>{
        var type = settings.getInt(SHOW_TYPE, 0)
        if(type == 0){
            if(settings.getInt(SHOW_FOUND,0) == 1 && settings.getInt(SORT_BY,0) == 0){
                return herpRepository.getAllHerpsNameFound()
            }else if(settings.getInt(SHOW_FOUND,0) == 1 && settings.getInt(SORT_BY,0) == 1){
                return herpRepository.getAllHerpsTypeFound()
            }else if(settings.getInt(SHOW_FOUND,0) == 0 && settings.getInt(SORT_BY,0) == 0){
                return herpRepository.getAllHerpsName()
            }else {
                return herpRepository.getAllHerpsType()
            }
        }else{
            type --
            if(settings.getInt(SHOW_FOUND,0) == 1 && settings.getInt(SORT_BY,0) == 0){
                return herpRepository.getAllHerpsNameFoundType(type)
            }else if(settings.getInt(SHOW_FOUND,0) == 1 && settings.getInt(SORT_BY,0) == 1){
                return herpRepository.getAllHerpsTypeFoundType(type)
            }else if(settings.getInt(SHOW_FOUND,0) == 0 && settings.getInt(SORT_BY,0) == 0){
                return herpRepository.getAllHerpsNameType(type)
            }else {
                return herpRepository.getAllHerpsTypeType(type)
            }
        }

    }


}
