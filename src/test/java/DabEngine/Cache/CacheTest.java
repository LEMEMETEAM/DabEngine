package DabEngine.Cache;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import DabEngine.Cache.InMemoryCache;

public class CacheTest {
    
    @Test
    public void checkIfObjectCanBeAddedAndRetrived(){
        InMemoryCache cache = new InMemoryCache.Builder().initialSize(16).maxSize(16).build();
        String obj = "lol";
        for(int i = 0; i < 16; i++){
            cache.add("string" + i, obj + i);
        }
        assertEquals(cache.get("string0"), "lol0");
        assertEquals(cache.get("string15"), "lol15");
        cache.add("string_final", "lol_final");
        assertEquals(cache.get("string14"), "lol14");
        assertNull(cache.get("string16"));
   }
}
