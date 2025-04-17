package clutter.core.coreTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import clutter.core.AnimationController;

class AnimationControllerTest {
    
    private AnimationController controller;
    private List<Double> values;
    
    @BeforeEach
    void setUp() {
        controller = new AnimationController();
        values = new ArrayList<>();
    }
    
    @AfterEach
    void tearDown() throws InterruptedException {
        // Add a brief delay after each test to ensure timer tasks are completed
        Thread.sleep(100);
    }
    
    @Test
    void testAnimationStartsAtBeginValue() throws InterruptedException {
        // Set up a latch to wait for at least one animation value
        CountDownLatch latch = new CountDownLatch(1);
        
        // Start the animation
        controller.animate(value -> {
            values.add(value);
            latch.countDown();
        }, 0.0, 100.0, 1.0);
        
        // Wait for at least one update
        assertTrue(latch.await(500, TimeUnit.MILLISECONDS), "Animation did not produce any values");
        
        // First value should be very close to the beginning value
        assertTrue(values.get(0) < 5.0, "First animation value should be close to begin value");
    }
    
    @Test
    void testAnimationProgresses() throws InterruptedException {
        // Set up a latch to wait for multiple animation values
        CountDownLatch latch = new CountDownLatch(3);
        
        // Start the animation
        controller.animate(value -> {
            values.add(value);
            if (values.size() <= 3) {
                latch.countDown();
            }
        }, 0.0, 100.0, 0.5);
        
        // Wait for at least three updates
        assertTrue(latch.await(500, TimeUnit.MILLISECONDS), "Animation did not produce enough values");
        
        // Values should increase over time (animation progresses)
        assertTrue(values.get(0) < values.get(1), "Animation should progress");
        assertTrue(values.get(1) < values.get(2), "Animation should progress");
    }
    
    @Test
    void testAnimationWithNegativeValues() throws InterruptedException {
        // Set up a latch to wait for at least one animation value
        CountDownLatch latch = new CountDownLatch(1);
        
        // Start the animation with negative values
        controller.animate(value -> {
            values.add(value);
            latch.countDown();
        }, -100.0, -50.0, 1.0);
        
        // Wait for at least one update
        assertTrue(latch.await(500, TimeUnit.MILLISECONDS), "Animation did not produce any values");
        
        // First value should be close to -100
        assertTrue(values.get(0) < -95.0, "First animation value should be close to begin value");
    }
    
    @Test
    void testAnimationWithReverseValues() throws InterruptedException {
        // Set up a latch to wait for multiple animation values
        CountDownLatch latch = new CountDownLatch(3);
        
        // Start animation that decreases instead of increases
        controller.animate(value -> {
            values.add(value);
            if (values.size() <= 3) {
                latch.countDown();
            }
        }, 100.0, 0.0, 0.5);
        
        // Wait for at least three updates
        assertTrue(latch.await(500, TimeUnit.MILLISECONDS), "Animation did not produce enough values");
        
        // Values should decrease over time
        assertTrue(values.get(0) > values.get(1), "Animation should progress downward");
        assertTrue(values.get(1) > values.get(2), "Animation should progress downward");
    }
    
    @Test
    void testEasingFunction() throws InterruptedException {
        // Set up a latch to wait for enough animation values to assess easing
        CountDownLatch latch = new CountDownLatch(10);
        
        // Start the animation
        controller.animate(value -> {
            values.add(value);
            if (values.size() <= 10) {
                latch.countDown();
            }
        }, 0.0, 100.0, 0.5);
        
        // Wait for enough updates
        assertTrue(latch.await(1000, TimeUnit.MILLISECONDS), "Animation did not produce enough values");
        
        // Get the first few differences
        double firstDiff = values.get(1) - values.get(0);
        double lastDiff = values.get(values.size() - 1) - values.get(values.size() - 2);
        
        // With ease-out, animation should decelerate (rate of change decreases)
        // The difference between consecutive values should decrease
        assertTrue(firstDiff > lastDiff, "Animation should use ease-out function");
    }
    
    @Test
    void testZeroDuration() throws InterruptedException {
        // For extremely short durations, animation should still work
        CountDownLatch latch = new CountDownLatch(1);
        
        controller.animate(value -> {
            values.add(value);
            latch.countDown();
        }, 0.0, 100.0, 0.01);
        
        // Wait briefly
        assertTrue(latch.await(500, TimeUnit.MILLISECONDS), "Animation did not produce any values");
        
        // Should have at least one value
        assertFalse(values.isEmpty(), "Animation should produce at least one value");
    }
}