package org.example.di

import dagger.Module
import dagger.Provides
import org.example.subtask.application.SubtaskService
import org.example.subtask.domain.repository.SubtaskRepository
import org.example.subtask.infrastructure.repository.SubtaskRepositoryImpl
import org.example.task.application.TaskService
import org.example.task.domain.repository.TaskRepository
import org.example.task.infrastructure.repository.ExposedTaskRepository

@Module
class AppModule {

    @Provides
    fun provideTaskRepository(): TaskRepository = ExposedTaskRepository()

    @Provides
    fun provideSubtaskRepository(): SubtaskRepository = SubtaskRepositoryImpl()
}
