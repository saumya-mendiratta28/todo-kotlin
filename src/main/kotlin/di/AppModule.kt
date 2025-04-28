package org.example.di

import dagger.Module
import dagger.Provides
import org.example.subtask.domain.repository.SubtaskRepository
import org.example.subtask.infrastructure.repository.SubtaskRepositoryImpl
import org.example.task.domain.repository.TaskRepository
import org.example.task.infrastructure.repository.TaskRepositoryImpl

@Module
class AppModule {

    @Provides
    fun provideTaskRepository(): TaskRepository = TaskRepositoryImpl()

    @Provides
    fun provideSubtaskRepository(): SubtaskRepository = SubtaskRepositoryImpl()
}
