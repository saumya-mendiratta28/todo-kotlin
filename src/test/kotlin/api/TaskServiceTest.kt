package api

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.example.task.application.TaskService
import org.example.task.domain.model.Task
import org.example.task.domain.usecase.TaskUseCase
import org.example.task.presentation.dto.TaskRequest
import org.example.task.presentation.response.TaskListResponse
import org.joda.time.DateTime
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TaskServiceTest {

    private lateinit var taskUseCase: TaskUseCase
    private lateinit var taskService: TaskService
    private lateinit var call: ApplicationCall

    @BeforeEach
    fun setup() {
        taskUseCase = mockk()
        call = mockk(relaxed = true)
        taskService = TaskService(taskUseCase)
    }

    @Test
    fun `test getAll`() = runTest {
        val tasks = listOf(Task(1, "Test", "Test Desc", DateTime.now().toString()))
        coEvery { taskUseCase.getAll() } returns tasks

        taskService.getAll(call)

        coVerify { call.respond(TaskListResponse(tasks)) }
    }

    @Test
    fun `test getById with valid id`() = runTest {
        val task = Task(1, "Test", "Test Desc", DateTime.now().toString())
        every { call.parameters["id"] } returns "1"
        coEvery { taskUseCase.getById(1) } returns task

        taskService.getById(call)

        coVerify { call.respond(task) }
    }

    @Test
    fun `test getById with invalid id`() = runTest {
        every { call.parameters["id"] } returns "abc"

        taskService.getById(call)

        coVerify { call.respond(HttpStatusCode.BadRequest, "Invalid ID format") }
    }

    @Test
    fun `test getById when task not found`() = runTest {
        every { call.parameters["id"] } returns "1"
        coEvery { taskUseCase.getById(1) } returns null

        taskService.getById(call)

        coVerify { call.respond(HttpStatusCode.NotFound, "Task not found") }
    }


//    @Test
//    fun `test create successfully`() = runTest {
//        // Given
//        val request = TaskRequest("Test Title", "Test Description")
//        val expectedTask = Task(
//            id = 0,
//            title = request.title,
//            description = request.description,
//            createdAt = DateTime.now().toString()
//        )
//        val savedTask = expectedTask.copy(id = 1) // Simulate saved task with ID
//
//        // Mock the receive call
//        coEvery { call.receive<TaskRequest>() } returns request
//        coEvery { taskUseCase.save(any()) } returns savedTask
//
//        // When
//        taskService.create(call)
//
//        // Then
//        coVerify {
//            call.receive<TaskRequest>()
//            taskUseCase.save(withArg { task ->
//                task.title == request.title &&
//                        task.description == request.description &&
//                        task.id == 0
//            })
//            call.respond(HttpStatusCode.Created, savedTask)
//        }
//    }

    @Test
    fun `test update with invalid id`() = runTest {
        every { call.parameters["id"] } returns "notANumber"

        taskService.update(call)

        coVerify { call.respond(HttpStatusCode.BadRequest, "Invalid ID format") }
    }


    @Test
    fun `test delete success`() = runTest {
        every { call.parameters["id"] } returns "1"
        coEvery { taskUseCase.delete(1) } returns true

        taskService.delete(call)

        coVerify { call.respond(HttpStatusCode.OK, "Task deleted successfully") }
    }

    @Test
    fun `test delete with invalid id`() = runTest {
        every { call.parameters["id"] } returns "bad"

        taskService.delete(call)

        coVerify { call.respond(HttpStatusCode.BadRequest, "Invalid ID format") }
    }

    @Test
    fun `test delete when task not found`() = runTest {
        every { call.parameters["id"] } returns "1"
        coEvery { taskUseCase.delete(1) } returns false

        taskService.delete(call)

        coVerify { call.respond(HttpStatusCode.NotFound, "Task not found") }
    }
}
