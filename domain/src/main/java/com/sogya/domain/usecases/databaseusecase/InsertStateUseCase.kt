package com.sogya.domain.usecases.databaseusecase

import com.sogya.domain.models.StateDomain
import com.sogya.domain.repository.LocalDataBaseRepository

class InsertStateUseCase(
    private val localDataBaseRepository: LocalDataBaseRepository
) {
    fun invoke(states: List<StateDomain>) = localDataBaseRepository.insertState(states)
}