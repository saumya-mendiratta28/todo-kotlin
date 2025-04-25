package org.example.di

import dagger.Component
import org.example.subtask.application.SubtaskService
import org.example.task.application.TaskService
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun taskService(): TaskService
    fun subtaskService(): SubtaskService
}