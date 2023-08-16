package ru.sogya.projects.domovoy.dialogs.state

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sogya.domain.usecases.databaseusecase.groups.GetAllGroupByOwnerUseCase
import com.sogya.domain.usecases.databaseusecase.states.InsertStateUseCase
import com.sogya.domain.usecases.sharedpreferences.GetStringPrefsUseCase
import com.sogya.domain.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import ru.sogya.projects.domovoy.models.StatePresenation
import javax.inject.Inject

@HiltViewModel
class AddStateDialogVM @Inject constructor(
    getStringPrefsUseCase: GetStringPrefsUseCase,
    private val getAllGroupByOwnerUseCase: GetAllGroupByOwnerUseCase,
    private val insertStateUseCase: InsertStateUseCase
) : ViewModel() {
    private var groupsLiveData: MutableLiveData<Array<String?>> = MutableLiveData()
    private var ownerId: String? = null
    private var groupIdList:IntArray?= null
    private var groupId:Int? = null

    init {
        ownerId = getStringPrefsUseCase.invoke(Constants.SERVER_URI)
        getAlGroupsByOwner()
    }

    fun addStatesToDataBase(
        states: HashSet<StatePresenation>,
        listener: ((success:Boolean) -> Unit)? = null
    ) {
        if (groupId != null) {
            states.forEach {
                it.ownerId = ownerId.toString()
                it.groupId = groupId as Int
            }
            viewModelScope.launch(Dispatchers.IO) {
                insertStateUseCase.invoke(states.toList())
            }
            listener?.invoke(true)
        }else{
            listener?.invoke(false)
        }
    }

    private fun getAlGroupsByOwner() {
        viewModelScope.launch {
            ownerId?.let { s ->
                getAllGroupByOwnerUseCase(ownerId = s).flowOn(Dispatchers.IO)
                    .collect { stateGroupDomains ->
                        val groupTagList = arrayOfNulls<String>(
                            stateGroupDomains.size
                        )
                        groupIdList = IntArray(size = stateGroupDomains.size)
                        var counter = 0
                        stateGroupDomains.forEach {
                            groupTagList[counter] = it.groupTag
                            groupIdList!![counter] = it.groupId
                            counter++
                        }
                        groupsLiveData.postValue(groupTagList)
                    }
            }
        }
    }

    fun getGroupsLiveData(): LiveData<Array<String?>> = groupsLiveData
    fun setSelectedGroup(id: Int) {
        groupId = groupIdList?.get(id)
    }
}