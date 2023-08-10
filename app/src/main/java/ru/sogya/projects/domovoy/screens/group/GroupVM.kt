package ru.sogya.projects.domovoy.screens.group

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.sogya.domain.models.StateGroupDomain
import com.sogya.domain.usecases.databaseusecase.groups.GetAllGroupByOwnerUseCase
import com.sogya.domain.usecases.sharedpreferences.GetStringPrefsUseCase
import com.sogya.domain.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.sogya.projects.domovoy.app.App
import ru.sogya.projects.domovoy.workers.EventWorker
import ru.sogya.projects.domovoy.workers.GetZonesWorker
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class GroupVM @Inject constructor(
    getStringPrefsUseCase: GetStringPrefsUseCase,
    getAllGroupByOwnerUseCase: GetAllGroupByOwnerUseCase,
) : ViewModel() {
    private var groupsLiveData: LiveData<List<StateGroupDomain>> = MutableLiveData()

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

        val ownerId = getStringPrefsUseCase.invoke(Constants.SERVER_URI)
        groupsLiveData = getAllGroupByOwnerUseCase.invoke(ownerId)
    }

    fun getGroupsLiveData(): LiveData<List<StateGroupDomain>> = groupsLiveData
}