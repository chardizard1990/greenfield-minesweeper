package com.minesweeper;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Basic sanity test to verify test framework is configured correctly.
 */
public class ProjectSetupTest {

    @Test
    public void testProjectSetupComplete() {
        // Simple sanity check to verify Maven and JUnit 5 are working
        assertTrue(true, "Project setup verification passed");
    }

    @Test
    public void testJavaVersion() {
        String javaVersion = System.getProperty("java.version");
        System.out.println("Running on Java version: " + javaVersion);
        // Verify Java 11+ (supports 11, 17, 21, 27, etc.)
        int majorVersion = Runtime.version().feature();
        assertTrue(majorVersion >= 11, "Java 11+ required, got version " + majorVersion);
    }
}
