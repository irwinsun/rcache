package org.wstorm.rcache.utils;

import com.google.common.collect.Maps;
import org.junit.Before;
import org.junit.Test;
import org.wstorm.rcache.annotation.CacheConfig;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.wstorm.rcache.utils.CacheUtils.defaultCacheConfig;

/**
 * @author sunyp
 * @version 1.0
 * @created 2016年05月10日
 */
public class CacheUtilsTest {
    static final String keyPrefix = "cacheUtils";

    static final int expiredTime = 180;

    static final String region = "junit";
    private static final String ID = "MyID001";

    @Test
    public void getCacheAnnotation() throws Exception {
        assertThat(CacheUtils.getCacheAnnotation(TestCacheObject.class)).isNotNull();
        assertThat(CacheUtils.getCacheAnnotation(null).annotationType()).isEqualTo(defaultCacheConfig.annotationType());
        assertThat(CacheUtils.getCacheAnnotation(Object.class)).isNull();
    }

    @Test
    public void getCacheRegion() throws Exception {
        assertThat(CacheUtils.getCacheRegion(TestCacheObject.class)).isEqualTo(region);
        assertThat(CacheUtils.getCacheRegion(null)).isEqualTo(defaultCacheConfig.region());
        assertThat(CacheUtils.getCacheRegion(String.class)).isNull();
    }

    @Test
    public void getCacheExpiredTime() throws Exception {
        assertThat(CacheUtils.getCacheExpiredTime(TestCacheObject.class)).isEqualTo(expiredTime);
        assertThat(CacheUtils.getCacheExpiredTime(null)).isZero();
        assertThat(CacheUtils.getCacheExpiredTime(String.class)).isZero();
    }

    @Test
    public void getCacheKeyPrefix() throws Exception {
        assertThat(CacheUtils.getCacheKeyPrefix(TestCacheObject.class)).isEqualTo(keyPrefix);
        assertThat(CacheUtils.getCacheKeyPrefix(null)).isEqualTo(defaultCacheConfig.keyPrefix());
        assertThat(CacheUtils.getCacheKeyPrefix(String.class)).isNull();
    }

    @Test
    public void genCacheKey() throws Exception {

        CacheConfig cacheConfig = CacheUtils.getCacheAnnotation(TestCacheObject.class);
        String cacheKey = CacheUtils.genCacheKey(cacheConfig, ID);
        assertThat(cacheKey).isNotNull();
        assertThat(cacheKey).startsWith(CacheUtils.getCacheRegion(TestCacheObject.class));
        assertThat(cacheKey).isNotEqualTo(ID);
        assertThat(CacheUtils.genCacheKey(null, ID)).isEqualTo(ID);

        assertThat(CacheUtils.genCacheKey(cacheConfig, cacheKey)).isEqualTo(cacheKey);

        assertThat(CacheUtils.getIdByCacheKey(cacheConfig, cacheKey)).isEqualTo(ID);

        assertThat(CacheUtils.getIdByCacheKey(null, cacheKey)).isEqualTo(cacheKey);
        assertThat(CacheUtils.getIdByCacheKey(null, ID)).isEqualTo(ID);

    }

    @Test
    public void genCacheKeys() throws Exception {
        CacheConfig cacheConfig = CacheUtils.getCacheAnnotation(TestCacheObject.class);
        List<String> ids = Arrays.asList(ID, ID, ID);
        List<String> cacheKeys = CacheUtils.genCacheKeys(cacheConfig, ids);
        assertThat(cacheKeys).isNotNull();
        assertThat(cacheKeys.size()).isEqualTo(3);
        assertThat(
                CacheUtils.genCacheKeys(null, ids)
        ).isEqualTo(ids);
    }

    @Test
    public void concat() throws Exception {
        String data = CacheUtils.concat("a", "b", "c");
        assertThat(data).isEqualTo("a:b:c");
    }


    @Test
    public void getBulkValueByIds() throws Exception {
        List<String> ids = Arrays.asList("z", "k", "c", "f");
        Map<String, Integer> bulk = Maps.newHashMap();
        bulk.put("c", 2);
        bulk.put("k", 5);
        bulk.put("z", 9);
        bulk.put("f", 7);

        List<Integer> days = CacheUtils.getBulkValueByIds(ids, bulk);
        assertThat(days.get(0)).isEqualTo(9);
        assertThat(days.get(1)).isEqualTo(5);
        assertThat(days.get(2)).isEqualTo(2);
        assertThat(days.get(3)).isEqualTo(7);
    }

    @CacheConfig(keyPrefix = keyPrefix, expiredTime = expiredTime, region = region)
    private class TestCacheObject {

    }

}