package service;

import org.junit.jupiter.api.Test;
import service.InMemoryHistoryManager;
import service.InMemoryTaskManager;
import service.Managers;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    @Test
    public void shouldBeTheSameClass() {
        assertSame(InMemoryTaskManager.class, Managers.getDefault().getClass());
        assertSame(InMemoryHistoryManager.class,Managers.getDefaultHistory().getClass());
    }
}