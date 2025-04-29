package data

import org.example.task.domain.model.Task
import org.example.task.infrastructure.persistence.TaskTable
import org.example.task.infrastructure.repository.TaskRepositoryImpl
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.junit.jupiter.api.*
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TaskRepositoryImplTest {

    private val repository = TaskRepositoryImpl()

    @BeforeAll
    fun setupDb() {
        Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;", driver = "org.h2.Driver")
        transaction {
            SchemaUtils.create(TaskTable)
        }
    }

    @AfterEach
    fun cleanDb() {
        transaction {
            TaskTable.deleteAll()
        }
    }

    @Test
    fun `should save and retrieve a task`() {
        val now = DateTime.now().toString()
        val task = Task(0, "Test Task", "Description", now)

        val saved = repository.save(task)
        val fetched = repository.getById(saved.id)

        assertNotNull(fetched)
        assertEquals(saved.id, fetched!!.id)
        assertEquals("Test Task", fetched.title)
    }

    @Test
    fun `should get all tasks`() {
        val now = DateTime.now().toString()
        val task1 = repository.save(Task(0, "Task 1", "Desc 1", now))
        val task2 = repository.save(Task(0, "Task 2", "Desc 2", now))

        val all = repository.getAll()

        assertEquals(2, all.size)
        assertTrue(all.any { it.id == task1.id })
        assertTrue(all.any { it.id == task2.id })
    }

    @Test
    fun `should update existing task`() {
        val now = DateTime.now().toString()
        val task = repository.save(Task(0, "Old", "Old", now))

        val updated = repository.update(task.id, task.copy(title = "New Title", description = "New Desc"))

        assertNotNull(updated)
        assertEquals("New Title", updated!!.title)
        assertEquals("New Desc", updated.description)
    }

    @Test
    fun `should return null when updating non-existing task`() {
        val now = DateTime.now().toString()
        val result = repository.update(999, Task(999, "X", "Y", now))

        assertNull(result)
    }

    @Test
    fun `should delete task`() {
        val now = DateTime.now().toString()
        val task = repository.save(Task(0, "Delete Me", "To Delete", now))

        val deleted = repository.delete(task.id)
        val fetched = repository.getById(task.id)

        assertTrue(deleted)
        assertNull(fetched)
    }
}
