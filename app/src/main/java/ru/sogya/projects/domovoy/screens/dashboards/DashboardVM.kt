package ru.sogya.projects.domovoy.screens.dashboards

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sogya.data.models.requests.CallService
import com.sogya.data.models.requests.CallServiceData
import com.sogya.data.models.requests.CallServiceTarget
import com.sogya.domain.usecases.databaseusecase.states.DeleteStateUseCase
import com.sogya.domain.usecases.databaseusecase.states.GetAllByGroupIdUseCase
import com.sogya.domain.usecases.websockets.SendMessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.sogya.projects.domovoy.models.StatePresentation
import ru.sogya.projects.domovoy.models.mappers.ListStateMapper
import javax.inject.Inject

@HiltViewModel
class DashboardVM @Inject constructor(
    private val sendMessageUseCase: SendMessageUseCase,
    private val getAllByGroup: GetAllByGroupIdUseCase,
    private val deleteUseCase: DeleteStateUseCase,
) : ViewModel() {
    private var itemsLiveData = MutableLiveData<List<StatePresentation>>()

    companion object {
        private var ID_SERVICE_COUNT = 24
    }

    fun getGroupStates(groupId: Int) {
        viewModelScope.launch {
            getAllByGroup(groupId).flowOn(Dispatchers.IO).catch {
                Log.d("Error", it.message.toString())
            }.map {
                ListStateMapper(it).map()
            }.collect {
                itemsLiveData.postValue(it)
            }
        }
    }

    fun getItemsLiveDat()
            : LiveData<List<StatePresentation>> = itemsLiveData

    fun callSwitchService(stateId: String, switchState: String) {
        val serviceDomain = stateId.substringBefore(".")
        val targetDevice = CallServiceTarget(stateId)
        val service =
            CallService(
                id = ID_SERVICE_COUNT,
                domain = serviceDomain,
                service = switchState,
                target = targetDevice
            )
        println(service)
        sendMessageUseCase.invoke(service)
        ID_SERVICE_COUNT++
    }

    fun deleteState(stateId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteUseCase.invoke(stateId)
        }
    }

    fun callMediaPLayerService(stateId: String, command: String) {
        val serviceDomain = stateId.substringBefore(".")
        val targetDevice = CallServiceTarget(stateId)
        val service =
            CallService(
                id = ID_SERVICE_COUNT,
                domain = serviceDomain,
                service = command,
                target = targetDevice
            )
        println(service)
        sendMessageUseCase.invoke(service)
        ID_SERVICE_COUNT++
    }

    fun changeCoverValue(stateId: String, value: Int) {
        val serviceDomain = stateId.substringBefore(".")
        val targetDevice = CallServiceTarget(stateId)
        val serviceData = CallServiceData(value)
        val service =
            CallService(
                id = ID_SERVICE_COUNT,
                domain = serviceDomain,
                service = "set_cover_position",
                target = targetDevice,
                serviceData = serviceData
            )
        println(service)
        sendMessageUseCase.invoke(service)
        ID_SERVICE_COUNT++
    }
}
