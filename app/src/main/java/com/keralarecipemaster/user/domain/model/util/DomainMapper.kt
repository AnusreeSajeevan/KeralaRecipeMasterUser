package com.keralarecipemaster.user.domain.model.util

interface DomainMapper<T, DomainModel> {
    fun toDomainModel(t: T): DomainModel
    fun fromDomainModel(domainModel: DomainModel): T
}
