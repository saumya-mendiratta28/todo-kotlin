package domain

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.task.domain.model.Task
import org.example.task.domain.repository.TaskRepository
import org.example.task.domain.usecase.TaskUseCase
import org.joda.time.DateTime
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TaskUseCaseTest {

    private lateinit var repository: TaskRepository
    private lateinit var useCase: TaskUseCase

    @BeforeEach
    fun setUp() {
        repository = mockk()
        useCase = TaskUseCase(repository)
    }

    @Test
    fun `getAll should return all tasks`() {
        val tasks = listOf(
            Task(1, "Task 1", "Desc 1", DateTime.now().toString()),
            Task(2, "Task 2", "Desc 2", DateTime.now().toString())
        )
        every { repository.getAll() } returns tasks

        val result = useCase.getAll()

        assertEquals(tasks, result)
        verify { repository.getAll() }
    }

    @Test
    fun `getById should return correct task`() {
        val task = Task(1, "Task 1", "Desc 1", DateTime.now().toString())
        every { repository.getById(1) } returns task

        val result = useCase.getById(1)

        assertEquals(task, result)
        verify { repository.getById(1) }
    }

    @Test
    fun `save should save and return task`() {
        val task = Task(0, "New Task", "New Desc", DateTime.now().toString())
        val savedTask = task.copy(id = 1)
        every { repository.save(task) } returns savedTask

        val result = useCase.save(task)

        assertEquals(savedTask, result)
        verify { repository.save(task) }
    }

    @Test
    fun `update should update and return task`() {
        val task = Task(1, "Updated Task", "Updated Desc", DateTime.now().toString())
        every { repository.update(1, task) } returns task

        val result = useCase.update(1, task)

        assertEquals(task, result)
        verify { repository.update(1, task) }
    }

    @Test
    fun `delete should return true when successful`() {
        every { repository.delete(1) } returns true

        val result = useCase.delete(1)

        assertTrue(result)
        verify { repository.delete(1) }
    }

    @Test
    fun `delete should return false when not successful`() {
        every { repository.delete(2) } returns false

        val result = useCase.delete(2)

        assertFalse(result)
        verify { repository.delete(2) }
    }
}
