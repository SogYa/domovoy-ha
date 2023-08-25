package com.sogya.domain.usecases.databaseusecase.states

import com.sogya.domain.repository.LocalDataBaseRepository

class GetStateByIdFlowUseCase(
    private val repository: LocalDataBaseRepository
) {
    operator fun invoke(stateId: String) = repository.getStateByIdFlow(stateId)
}