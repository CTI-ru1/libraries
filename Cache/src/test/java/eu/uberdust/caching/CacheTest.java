package eu.uberdust.caching;


import org.apache.log4j.Logger;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by IntelliJ IDEA.
 * User: akribopo
 * Date: 3/5/12
 * Time: 1:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class CacheTest {

    private final static Logger LOGGER = Logger.getLogger(CacheTest.class);

    static int times_in_someMethod = 0;
    static int times_in_cacheTest = 0;
    static int times_in_addNodeReading = 0;


    @Cachable
    int someMethod(int i) {
        times_in_someMethod++;
        return times_in_someMethod;
    }

    @Cachable
    public final int cacheTest(final int input) {
        LOGGER.info("Executing function cacheTest");
        times_in_cacheTest++;
        return input;
    }

    @EvictCache(cacheName = "")
    public void addNodeReading(int i) {
        times_in_addNodeReading++;
        LOGGER.info("adding :" + i);
    }

    @Test
    public void testCacheable() throws InterruptedException {
        LOGGER.info("testCacheable");
        CacheTest cacheTest = new CacheTest();
        for (int j = 0; j < 20; j++) {
            cacheTest.someMethod(1);
        }
        assertEquals(1, times_in_someMethod);
    }

    @Test
    public void testCacheOperations() throws InterruptedException {
        LOGGER.info("testCacheOperations");

        CacheTest cacheTest = new CacheTest();
        for (int j = 0; j < 20; j++) {
            cacheTest.addNodeReading(j);
        }
        assertEquals(20, times_in_addNodeReading);
        LOGGER.info("added Values");
        for (int j = 0; j < 20; j++) {
            for (int i = 0; i < 20; i++) {
                cacheTest.cacheTest(i);
            }
        }
        assertEquals(20, times_in_cacheTest);
        LOGGER.info("got Cache Hits");
        cacheTest.addNodeReading(99);
        LOGGER.info("evicted Caches");
        for (int j = 0; j < 20; j++) {
            for (int i = 0; i < 20; i++) {
                cacheTest.cacheTest(i);
            }
        }
        assertEquals(40, times_in_cacheTest);
        LOGGER.info("got Empty Cache");
    }

}
