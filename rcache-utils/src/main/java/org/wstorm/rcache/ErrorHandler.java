package org.wstorm.rcache;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 处理处理,默认提供一个处理方法
 *
 * @author sunyp
 * @version 1.0
 * @created 2016年05月08日
 */
public interface ErrorHandler {

    Logger log = LoggerFactory.getLogger(ErrorHandler.class);

    default void handleException(Exception e, String key) {
        log.warn("ErrorHandler receive an exception with key={} | exception={}", key, e);
    }
}
