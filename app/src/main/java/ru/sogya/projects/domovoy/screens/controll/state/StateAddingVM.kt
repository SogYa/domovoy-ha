package ru.sogya.projects.domovoy.screens.controll.state

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sogya.domain.usecases.network.GetStatesUseCase
import com.sogya.domain.usecases.sharedpreferences.GetStringPrefsUseCase
import com.sogya.domain.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import ru.sogya.projects.domovoy.models.StatePresenation
import ru.sogya.projects.domovoy.models.mappers.StateMapper
import ru.sogya.projects.domovoy.utils.VisibilityStates
import javax.inject.Inject

@HiltViewModel
class StateAddingVM @Inject constructor(
    getStatesUseCase: GetStatesUseCase,
    getStringPrefsUseCase: GetStringPrefsUseCase,
) : ViewModel() {
    private val statesLiveData = MutableLiveData<List<StatePresenation>>()
    private val loadingViewLiveData = MutableLiveData<Int>()


    init {
        val url = getStringPrefsUseCase.invoke(Constants.SERVER_URI)
        val token = getStringPrefsUseCase.invoke(Constants.AUTH_TOKEN)
        viewModelScope.launch {
            getStatesUseCase.invoke(url, token).flowOn(Dispatchers.IO)
                .catch {
                    Log.d("StatesError", it.message.toString())
                }.collect { stateDomains ->
                    statesLiveData.postValue(stateDomains.map {
                        StateMapper(it).map()
                    })
                    loadingViewLiveData.postValue(VisibilityStates.GONE.visibility)
                }
        }
    }

    fun getStatesLiveData(): LiveData<List<StatePresenation>> = statesLiveData
    fun getLoadingLiveData(): LiveData<Int> = loadingViewLiveData
}
