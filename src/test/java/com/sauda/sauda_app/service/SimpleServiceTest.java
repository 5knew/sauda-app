package com.sauda.sauda_app.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SimpleServiceTest {

    @Test
    void testBasicMath() {
        // Простой тест без Spring контекста
        int result = 2 + 2;
        assertEquals(4, result, "2 + 2 должно быть 4");
    }

    @Test
    void testStringOperations() {
        String message = "Hello, Sauda-DB!";
        assertNotNull(message);
        assertTrue(message.contains("Sauda-DB"));
        assertEquals(16, message.length()); // "Hello, Sauda-DB!" = 16 символов
    }

    @Test
    void testBooleanLogic() {
        boolean isTrue = true;
        boolean isFalse = false;
        
        assertTrue(isTrue);
        assertFalse(isFalse);
        assertNotEquals(isTrue, isFalse);
    }
}
