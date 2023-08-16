package ru.sogya.projects.domovoy.screens.group

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.sogya.domain.models.StateGroupDomain
import com.sogya.domain.usecases.databaseusecase.groups.GetAllGroupByOwnerUseCase
import com.sogya.domain.usecases.sharedpreferences.GetStringPrefsUseCase
import com.sogya.domain.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import ru.sogya.projects.domovoy.app.App
import ru.sogya.projects.domovoy.workers.EventWorker
import ru.sogya.projects.domovoy.workers.GetZonesWorker
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class GroupVM @Inject constructor(
    private val getStringPrefsUseCase: GetStringPrefsUseCase,
    private val getAllGroupByOwnerUseCase: GetAllGroupByOwnerUseCase,
) : ViewModel() {
    private var groupsLiveData: MutableLiveData<List<StateGroupDomain>> = MutableLiveData()

    init {
        val updateStatesWork = PeriodicWorkRequestBuilder<EventWorker>(
            repeatInterval = 15,
            repeatIntervalTimeUnit = TimeUnit.MINUTES,
            flexTimeInterval = 15,
            flexTimeIntervalUnit = TimeUnit.MINUTES
        )
            .build()
        val getZonesWorker = OneTimeWorkRequestBuilder<GetZonesWorker>()
            .build()
        WorkManager.getInstance(App.getAppContext())
            .enqueue(
                listOf(updateStatesWork, getZonesWorker)
            )

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

    fun getGroupsLiveData(): LiveData<List<StateGroupDomain>> = groupsLiveData
}