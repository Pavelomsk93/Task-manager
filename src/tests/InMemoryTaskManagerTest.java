import org.junit.jupiter.api.BeforeEach;
import tracker.controllers.InMemoryTaskManager;
import tracker.controllers.Managers;



public class InMemoryTaskManagerTest extends TaskManagerTest <InMemoryTaskManager> {

    @BeforeEach
    void updateTaskManager() {
        taskManager = new InMemoryTaskManager();
    }
}