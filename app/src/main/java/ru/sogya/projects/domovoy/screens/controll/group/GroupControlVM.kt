package ru.sogya.projects.domovoy.screens.controll.group

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sogya.domain.models.StateGroupDomain
import com.sogya.domain.usecases.databaseusecase.groups.DeleteGroupUseCase
import com.sogya.domain.usecases.databaseusecase.groups.GetAllGroupByOwnerUseCase
import com.sogya.domain.usecases.sharedpreferences.GetStringPrefsUseCase
import com.sogya.domain.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupControlVM @Inject constructor(
    private val getStringPrefsUseCase: GetStringPrefsUseCase,
    private val getAllGroupByOwnerUseCase: GetAllGroupByOwnerUseCase,
    private val deleteGroupUseCase: DeleteGroupUseCase
) : ViewModel() {
    private var groupsLiveData: MutableLiveData<List<StateGroupDomain>> = MutableLiveData()

    init {
        getAlGroupsByOwner()
    }

    private fun getAlGroupsByOwner() {
        viewModelScope.launch {
            val ownerId = getStringPrefsUseCase.invoke(Constants.SERVER_URI)
            getAllGroupByOwnerUseCase(ownerId = ownerId).flowOn(Dispatchers.IO).collect {
                groupsLiveData.postValue(it)
            }
        }
    }

    fun deleteGroup(groupId: Int) {
        viewModelScope.launch {
            deleteGroupUseCase.invoke(groupId)
        }
    }

    fun getGroupsLiveData(): LiveData<List<StateGroupDomain>> = groupsLiveData
}