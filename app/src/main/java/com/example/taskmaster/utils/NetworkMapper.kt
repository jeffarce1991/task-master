package com.example.taskmaster.utils

import com.example.taskmaster.models.*

class NetworkMapper:
    EntityMapper<TaskDto, Task> {

    override fun mapFromDto(dto: TaskDto): Task {
        return Task(
            dto.id,
            dto.title,
            dto.description,
            dto.status,
            dto.dateCreated,
            dto.dateCompleted
            )
    }

    override fun mapToDto(model: Task): TaskDto {
        return TaskDto(
            model.id!!,
            model.title,
            model.description,
            model.status!!,
            model.dateCreated,
            model.dateCompleted!!
        )
    }

    fun mapFromDtoList(entities: List<TaskDto>): List<Task>{
        return entities.map { mapFromDto(it) }
    }
}