package de.comparus.opensource.longmap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LongMapImplTest {

    private LongMapImpl<String> longMap;
    private final long INIT_KEY = 1;
    private final String INIT_VALUE = "value1";
    private final long KEY1 = 77;
    private final String VALUE1 = "value77";
    private final long KEY2 = 88;
    private final String VALUE2 = "value88";
    private final long COLLISION_KEY1 = 17;
    private final String COLLISION_VALUE1 = "value17";
    private final long COLLISION_KEY2 = 17 + 16;
    private final String COLLISION_VALUE2 = "value17+16";

    @BeforeEach
    void initMap() {
        longMap = new LongMapImpl<>();
    }

    @Test
    void put() {
        longMap = new LongMapImpl<>(4, 0.4f);
        longMap.put(INIT_KEY + 1, INIT_VALUE + 1);
        longMap.put(INIT_KEY + 5, INIT_VALUE + 5);
        longMap.put(INIT_KEY + 2, INIT_VALUE + 2);
        longMap.put(INIT_KEY + 3, INIT_VALUE + 3);
        assertEquals(4, longMap.size());
    }

    @Test
    void get() {
        longMap.put(INIT_KEY, INIT_VALUE);
        assertEquals(INIT_VALUE, longMap.get(INIT_KEY));
    }

    @Test
    void containsKey() {
        longMap.put(INIT_KEY, INIT_VALUE);
        assertTrue(longMap.containsKey(INIT_KEY));
        assertFalse(longMap.containsKey(KEY1));
    }

    @Test
    void containsValue() {
        longMap.put(INIT_KEY, INIT_VALUE);
        assertTrue(longMap.containsValue(INIT_VALUE));
        assertFalse(longMap.containsValue(VALUE1));
    }

    @Test
    void removeNodeWithoutCollision() {
        longMap.put(INIT_KEY, INIT_VALUE);
        assertEquals(INIT_VALUE, longMap.remove(INIT_KEY));
        assertFalse(longMap.containsKey(INIT_KEY));
        assertEquals(0, longMap.size());
    }

    @Test
    void removeNodeWitCollisionOne() {
        longMap.put(INIT_KEY, INIT_VALUE);
        longMap.put(COLLISION_KEY1, COLLISION_VALUE1);
        assertEquals(INIT_VALUE, longMap.remove(INIT_KEY));
        assertFalse(longMap.containsKey(INIT_KEY));
        assertTrue(longMap.containsKey(COLLISION_KEY1));
        assertEquals(1, longMap.size());
    }

    @Test
    void removeNodeWitCollisionTwo() {
        longMap.put(INIT_KEY, INIT_VALUE);
        longMap.put(COLLISION_KEY1, COLLISION_VALUE1);
        longMap.put(COLLISION_KEY2, COLLISION_VALUE2);
        assertEquals(INIT_VALUE, longMap.remove(INIT_KEY));
        assertFalse(longMap.containsKey(INIT_KEY));
        assertTrue(longMap.containsKey(COLLISION_KEY1));
        assertEquals(2, longMap.size());
    }

    @Test
    void removeNodeWitCollisionThree() {
        longMap.put(INIT_KEY, INIT_VALUE);
        longMap.put(COLLISION_KEY1, COLLISION_VALUE1);
        longMap.put(COLLISION_KEY2, COLLISION_VALUE2);
        assertEquals(COLLISION_VALUE1, longMap.remove(COLLISION_KEY1));
        assertFalse(longMap.containsKey(COLLISION_KEY1));
        assertTrue(longMap.containsKey(INIT_KEY));
        assertTrue(longMap.containsKey(COLLISION_KEY2));
        assertEquals(2, longMap.size());
    }

    @Test
    void returnKeys() {
        longMap.put(INIT_KEY, INIT_VALUE);
        longMap.put(KEY1, VALUE1);
        longMap.put(KEY2, VALUE2);
        assertEquals(3, longMap.keys().length);
        assertEquals(INIT_KEY, longMap.keys()[0]);
        assertEquals(KEY2, longMap.keys()[1]);
        assertEquals(KEY1, longMap.keys()[2]);
    }

    @Test
    void returnValues() {
        longMap.put(INIT_KEY, INIT_VALUE);
        longMap.put(KEY1, VALUE1);
        longMap.put(KEY2, VALUE2);
        String[] values = longMap.values();
        assertEquals(3, values.length);
        assertEquals(INIT_VALUE, values[0]);
        assertEquals(VALUE2, values[1]);
        assertEquals(VALUE1, values[2]);
    }

    @Test
    void clearMap() {
        longMap.put(INIT_KEY, INIT_VALUE);
        longMap.put(KEY1, VALUE1);
        longMap.put(KEY2, VALUE2);
        longMap.clear();
        assertEquals(0, longMap.size());
        assertNull(longMap.get(INIT_KEY));
        assertNull(longMap.get(KEY1));
        assertNull(longMap.get(KEY2));
    }
}