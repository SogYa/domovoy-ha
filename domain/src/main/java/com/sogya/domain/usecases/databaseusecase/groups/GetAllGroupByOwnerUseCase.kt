package com.sogya.domain.usecases.databaseusecase.groups

import com.sogya.domain.repository.LocalDataBaseRepository

class GetAllGroupByOwnerUseCase(
    private val repository: LocalDataBaseRepository
) {
    suspend operator fun invoke(ownerId: String) = repository.getAllGroupsByOwner(ownerId)
}