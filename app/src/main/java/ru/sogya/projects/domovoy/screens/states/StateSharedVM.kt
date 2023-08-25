package ru.sogya.projects.domovoy.screens.states


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sogya.data.models.requests.CallService
import com.sogya.data.models.requests.CallServiceTarget
import com.sogya.domain.usecases.databaseusecase.states.GetStateByIdFlowUseCase
import com.sogya.domain.usecases.network.GetStateHistoryUseCase
import com.sogya.domain.usecases.sharedpreferences.GetStringPrefsUseCase
import com.sogya.domain.usecases.websockets.SendMessageUseCase
import com.sogya.domain.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.sogya.projects.domovoy.models.StatePresentation
import ru.sogya.projects.domovoy.models.mappers.StateMapper
import javax.inject.Inject

@HiltViewModel
class StateSharedVM @Inject constructor(
    private val getStateByIdUseCase: GetStateByIdFlowUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val getStateHistoryUseCase: GetStateHistoryUseCase,
    private val getStringPrefsUseCase: GetStringPrefsUseCase,
) : ViewModel() {
    private var stateLiveData = MutableLiveData<StatePresentation>()
    private val stateHistoryLiveData = MutableLiveData<List<Float>>()

    companion object {
        var ID_SERVICE_COUNT = 25
    }

    fun getState(stateId: String) {
        viewModelScope.launch {
            getStateByIdUseCase(stateId)
                .flowOn(Dispatchers.IO)
                .map {
                    StateMapper(it).map()
                }
                .collect {
                    stateLiveData.postValue(it)
                }
        }
    }

    fun getStateHisotry() {
        val baseUri = getStringPrefsUseCase(Constants.SERVER_URI)
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

    fun getStateLiveData(): LiveData<StatePresentation> = stateLiveData
}