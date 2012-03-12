package eu.uberdust.caching;


import org.apache.log4j.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: akribopo
 * Date: 3/5/12
 * Time: 1:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class CacheTest {

    private final static Logger LOGGER = Logger.getLogger(CacheTest.class);

    @Cachable
    public final int cacheTest(final int input) {
        LOGGER.info("Executing function cacheTest");
        return input;
    }

    public static void main(String[] args) throws InterruptedException {
        CacheTest cacheTest = new CacheTest();
        for (int j = 0; j < 20; j++) {
            for (int i = 0; i < 10; i++) {
                  cacheTest.addNodeReading(i);
                //cacheTest.cacheTest(i);
            }
            Thread.sleep(1000);
            //cacheTest.addNodeReading(j);
        }

    }
    @EvictCache(cacheName = "oeo")
    public void addNodeReading(int i ){
        System.out.println(i);
    }
}
