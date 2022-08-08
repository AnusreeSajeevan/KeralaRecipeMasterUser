package com.keralarecipemaster.admin.domain.model.util

interface DomainMapper<T, DomainModel> {
    fun toDomainModel(t: T): DomainModel
    fun fromDomainModel(domainModel: DomainModel): T
}
