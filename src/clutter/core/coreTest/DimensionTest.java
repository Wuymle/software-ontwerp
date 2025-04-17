package clutter.core.coreTest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import clutter.core.Dimension;

class DimensionTest {

    @Test
    void testConstructor() {
        Dimension dim = new Dimension(10, 20);
        assertEquals(10, dim.x());
        assertEquals(20, dim.y());
    }
    
    @Test
    void testSquare() {
        Dimension dim = Dimension.square(5);
        assertEquals(5, dim.x());
        assertEquals(5, dim.y());
    }
    
    @Test
    void testMin() {
        Dimension dim1 = new Dimension(10, 30);
        Dimension dim2 = new Dimension(20, 20);
        Dimension result = Dimension.min(dim1, dim2);
        assertEquals(10, result.x());
        assertEquals(20, result.y());
    }
    
    @Test
    void testMax() {
        Dimension dim1 = new Dimension(10, 30);
        Dimension dim2 = new Dimension(20, 20);
        Dimension result = Dimension.max(dim1, dim2);
        assertEquals(20, result.x());
        assertEquals(30, result.y());
    }
    
    @Test
    void testGetArea() {
        Dimension dim = new Dimension(10, 20);
        assertEquals(200, dim.getArea());
    }
    
    @Test
    void testAdd() {
        Dimension dim1 = new Dimension(10, 20);
        Dimension dim2 = new Dimension(30, 40);
        Dimension result = dim1.add(dim2);
        assertEquals(40, result.x());
        assertEquals(60, result.y());
    }
    
    @Test
    void testAddWithMaxValue() {
        Dimension dim1 = new Dimension(Integer.MAX_VALUE, 20);
        Dimension dim2 = new Dimension(30, Integer.MAX_VALUE);
        Dimension result = dim1.add(dim2);
        assertEquals(Integer.MAX_VALUE, result.x());
        assertEquals(Integer.MAX_VALUE, result.y());
    }
    
    @Test
    void testSubtract() {
        Dimension dim1 = new Dimension(50, 70);
        Dimension dim2 = new Dimension(20, 30);
        Dimension result = dim1.subtract(dim2);
        assertEquals(30, result.x());
        assertEquals(40, result.y());
    }
    
    @Test
    void testSubtractWithMaxValue() {
        Dimension dim1 = new Dimension(Integer.MAX_VALUE, 70);
        Dimension dim2 = new Dimension(20, Integer.MAX_VALUE);
        Dimension result = dim1.subtract(dim2);
        assertEquals(Integer.MAX_VALUE, result.x());
        assertEquals(Integer.MAX_VALUE, result.y());
    }
    
    @Test
    void testMultiplyInt() {
        Dimension dim = new Dimension(10, 20);
        Dimension result = dim.multiply(3);
        assertEquals(30, result.x());
        assertEquals(60, result.y());
    }
    
    @Test
    void testMultiplyIntWithMaxValue() {
        Dimension dim = new Dimension(Integer.MAX_VALUE, 20);
        Dimension result = dim.multiply(3);
        assertEquals(Integer.MAX_VALUE, result.x());
        assertEquals(60, result.y());
    }
    
    @Test
    void testMultiplyDouble() {
        Dimension dim = new Dimension(10, 20);
        Dimension result = dim.multiply(2.5);
        assertEquals(25, result.x());
        assertEquals(50, result.y());
    }
    
    @Test
    void testMultiplyDoubleWithMaxValue() {
        Dimension dim = new Dimension(Integer.MAX_VALUE, 20);
        Dimension result = dim.multiply(2.5);
        assertEquals(Integer.MAX_VALUE, result.x());
        assertEquals(50, result.y());
    }
    
    @Test
    void testDivide() {
        Dimension dim = new Dimension(30, 60);
        Dimension result = Dimension.divide(dim, 3);
        assertEquals(10, result.x());
        assertEquals(20, result.y());
    }
    
    @Test
    void testDivideWithMaxValue() {
        Dimension dim = new Dimension(Integer.MAX_VALUE, 60);
        Dimension result = Dimension.divide(dim, 3);
        assertEquals(Integer.MAX_VALUE, result.x());
        assertEquals(20, result.y());
    }
    
    @Test
    void testWithX() {
        Dimension dim = new Dimension(10, 20);
        Dimension result = dim.withX(30);
        assertEquals(30, result.x());
        assertEquals(20, result.y());
    }
    
    @Test
    void testWithY() {
        Dimension dim = new Dimension(10, 20);
        Dimension result = dim.withY(30);
        assertEquals(10, result.x());
        assertEquals(30, result.y());
    }
    
    @Test
    void testAddX() {
        Dimension dim = new Dimension(10, 20);
        Dimension result = dim.addX(5);
        assertEquals(15, result.x());
        assertEquals(20, result.y());
    }
    
    @Test
    void testAddXWithMaxValue() {
        Dimension dim = new Dimension(Integer.MAX_VALUE, 20);
        Dimension result = dim.addX(5);
        assertEquals(Integer.MAX_VALUE, result.x());
        assertEquals(20, result.y());
    }
    
    @Test
    void testAddY() {
        Dimension dim = new Dimension(10, 20);
        Dimension result = dim.addY(5);
        assertEquals(10, result.x());
        assertEquals(25, result.y());
    }
    
    @Test
    void testAddYWithMaxValue() {
        Dimension dim = new Dimension(10, Integer.MAX_VALUE);
        Dimension result = dim.addY(5);
        assertEquals(10, result.x());
        assertEquals(Integer.MAX_VALUE, result.y());
    }
    
    @Test
    void testMulX() {
        Dimension dim = new Dimension(10, 20);
        Dimension result = dim.mulX(3);
        assertEquals(30, result.x());
        assertEquals(20, result.y());
    }
    
    @Test
    void testMulXWithMaxValue() {
        Dimension dim = new Dimension(Integer.MAX_VALUE, 20);
        Dimension result = dim.mulX(3);
        assertEquals(Integer.MAX_VALUE, result.x());
        assertEquals(20, result.y());
    }
    
    @Test
    void testMulY() {
        Dimension dim = new Dimension(10, 20);
        Dimension result = dim.mulY(3);
        assertEquals(10, result.x());
        assertEquals(60, result.y());
    }
    
    @Test
    void testMulYWithMaxValue() {
        Dimension dim = new Dimension(10, Integer.MAX_VALUE);
        Dimension result = dim.mulY(3);
        assertEquals(10, result.x());
        assertEquals(Integer.MAX_VALUE, result.y());
    }
    
    @Test
    void testMulXDouble() {
        Dimension dim = new Dimension(10, 20);
        Dimension result = dim.mulX(2.5);
        assertEquals(25, result.x());
        assertEquals(20, result.y());
    }
    
    @Test
    void testMulXDoubleWithMaxValue() {
        Dimension dim = new Dimension(Integer.MAX_VALUE, 20);
        Dimension result = dim.mulX(2.5);
        assertEquals(Integer.MAX_VALUE, result.x());
        assertEquals(20, result.y());
    }
    
    @Test
    void testMulYDouble() {
        Dimension dim = new Dimension(10, 20);
        Dimension result = dim.mulY(2.5);
        assertEquals(10, result.x());
        assertEquals(50, result.y());
    }
    
    @Test
    void testMulYDoubleWithMaxValue() {
        Dimension dim = new Dimension(10, Integer.MAX_VALUE);
        Dimension result = dim.mulY(2.5);
        assertEquals(10, result.x());
        assertEquals(Integer.MAX_VALUE, result.y());
    }
    
    @Test
    void testContainsInside() {
        Dimension position = new Dimension(10, 10);
        Dimension size = new Dimension(100, 100);
        Dimension location = new Dimension(50, 50);
        assertTrue(Dimension.contains(position, size, location));
    }
    
    @Test
    void testContainsOutside() {
        Dimension position = new Dimension(10, 10);
        Dimension size = new Dimension(100, 100);
        Dimension location = new Dimension(200, 200);
        assertFalse(Dimension.contains(position, size, location));
    }
    
    @Test
    void testContainsBoundary() {
        Dimension position = new Dimension(10, 10);
        Dimension size = new Dimension(100, 100);
        // Bottom-right corner is at (110, 110)
        Dimension location = new Dimension(110, 110);
        assertTrue(Dimension.contains(position, size, location));
    }
    
    @Test
    void testToString() {
        Dimension dim = new Dimension(10, 20);
        assertEquals("(10, 20)", dim.toString());
    }
    
    @Test
    void testIsSmallerTrue() {
        Dimension dim1 = new Dimension(10, 20);
        Dimension dim2 = new Dimension(10, 30);
        assertTrue(dim1.isSmaller(dim2));
    }
    
    @Test
    void testIsSmallerFalse() {
        Dimension dim1 = new Dimension(20, 10);
        Dimension dim2 = new Dimension(10, 30);
        assertFalse(dim1.isSmaller(dim2));
    }
    
    @Test
    void testIsSmallerEqual() {
        Dimension dim1 = new Dimension(10, 20);
        Dimension dim2 = new Dimension(10, 20);
        assertTrue(dim1.isSmaller(dim2));
    }
    
    @Test
    void testRecordEquality() {
        Dimension dim1 = new Dimension(10, 20);
        Dimension dim2 = new Dimension(10, 20);
        assertEquals(dim1, dim2);
    }
    
    @Test
    void testRecordHashCode() {
        Dimension dim1 = new Dimension(10, 20);
        Dimension dim2 = new Dimension(10, 20);
        assertEquals(dim1.hashCode(), dim2.hashCode());
    }
}

