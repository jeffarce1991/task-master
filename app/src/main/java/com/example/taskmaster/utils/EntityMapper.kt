package com.example.taskmaster.utils

interface EntityMapper <Dto, Model>{

    fun mapFromDto(dto: Dto): Model

    fun mapToDto(model: Model): Dto
}