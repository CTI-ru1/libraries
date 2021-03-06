package eu.uberdust.caching;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import javax.servlet.http.HttpServletRequest;


@Aspect
public class CachingAspect {

    private static final Logger LOGGER = Logger.getLogger(CachingAspect.class);

    private final CacheManager singletonManager;

    public CachingAspect() {
        singletonManager = CacheManager.create();

    }

    /**
     * This pointcut matches all methods with a <code>@Cachable</code> annotation.
     */
    @Pointcut("execution(@Cachable * *.*(..))")
    @SuppressWarnings("unused")
    private void cache() {
    }


    /**
     * This pointcut matches all methods with a <code>@Cachable</code> annotation.
     */
    @Pointcut("execution(@Loggable * *.*(..))")
    @SuppressWarnings("unused")
    private void log() {
    }


    /**
     * Returns objects from the cache if necessary.
     */
    @Around("cache()")
    public Object aroundCache(final ProceedingJoinPoint thisJoinPoint) throws Throwable {
        final String thisJoinPointName = getJoinPointName(thisJoinPoint);
        final String thisJoinPointArgs = getJointPointArgs(thisJoinPoint);
        final String objName = thisJoinPointName + "-" + thisJoinPointArgs;
        LOGGER.debug("Cachings: " + objName);


        if (singletonManager.cacheExists(thisJoinPointName)) {
            final Cache cache = singletonManager.getCache(thisJoinPointName);

            if (cache.getKeysWithExpiryCheck().contains(thisJoinPointArgs.hashCode())) {
                LOGGER.debug(" HIT: " + cache.getName());
                return cache.get(thisJoinPointArgs.hashCode()).getObjectValue();
            } else {
                final Element element = new Element(thisJoinPointArgs.hashCode(), thisJoinPoint.proceed());
                cache.put(element);
                LOGGER.debug("MISS: " + cache.getName());
                return element.getObjectValue();
            }
        } else {
            singletonManager.addCacheIfAbsent("defaultCache");

            final Cache cache = singletonManager.getCache("defaultCache");

            if (cache.getKeysWithExpiryCheck().contains((objName).hashCode())) {

                LOGGER.debug("HIT: " + cache.getName());

                return cache.get((objName).hashCode()).getObjectValue();
            } else {
                final Element element = new Element((objName).hashCode(),
                        thisJoinPoint.proceed());
                LOGGER.debug("MISS: " + cache.getName());
                cache.put(element);
                return element.getObjectValue();
            }
        }
    }

    @Pointcut("@annotation( thisCachename )")
    @SuppressWarnings("unused")
    private void evictCache(EvictCache thisCachename) {
    }

    @Around("evictCache(thisCachename)")
    public Object processRequest(final ProceedingJoinPoint thisJoinPoint, EvictCache thisCachename) throws Throwable {
        if (thisJoinPoint.getKind().equals(ProceedingJoinPoint.METHOD_CALL)) {
            return thisJoinPoint.proceed();
        } else {

            if ("".equals(thisCachename.cacheName())) {
                for (String cache : CacheManager.getInstance().getCacheNames()) {
                    LOGGER.info("Evicting cache: " + cache);
                    CacheManager.getInstance().getCache(cache).removeAll();
                }
            } else {
                final String[] cacheNames = thisCachename.cacheName().split(",");
                for (final String cache : cacheNames) {
                    if (CacheManager.getInstance().cacheExists(cache)) {
                        CacheManager.getInstance().getCache(cache).removeAll();
                        LOGGER.info("Evicting cache: " + cache);
                    }
                }
            }
            return thisJoinPoint.proceed();
        }
    }


    /**
     * Logs the request to uberdust.
     */
    @Around("log()")
    public Object aroundLog(final ProceedingJoinPoint thisJoinPoint) throws Throwable {
        LOGGER.info(thisJoinPoint.getTarget().getClass() + "," + thisJoinPoint.getKind());
        if (thisJoinPoint.getArgs().length > 0) {
            for (int i = 0; i < thisJoinPoint.getArgs().length; i++) {
                if (thisJoinPoint.getArgs()[i] instanceof HttpServletRequest) {
                    final HttpServletRequest myRequest = (HttpServletRequest) thisJoinPoint.getArgs()[i];
                    final String requestUrl = myRequest.getRequestURL().toString();
                    LOGGER.info(myRequest.getMethod() + "," + requestUrl.substring(requestUrl.indexOf("rest")) + "," + myRequest.getRemoteAddr());
                }
            }

        }
        return thisJoinPoint.proceed();
    }


    /**
     * Convenience method that returns the class- and method-name for a given join point.
     */
    public final String getJoinPointName(final JoinPoint joinPoint) {
        return joinPoint.getThis().getClass().getCanonicalName() + "." + joinPoint.getSignature().getName();
    }

    /**
     * Returns the arguments of the current join point as a string.
     *
     * @param joinPoint
     * @return string representing the arguments of this join point
     */
    public final String getJointPointArgs(final JoinPoint joinPoint) {
        final StringBuilder buf = new StringBuilder();
        for (final Object arg : joinPoint.getArgs()) {
            buf.append(arg.getClass().getSimpleName()).append("-").append(arg.hashCode());
        }
        String var = buf.toString().replaceAll("\\$", "");
        LOGGER.debug(var);
        return var;
    }
}
