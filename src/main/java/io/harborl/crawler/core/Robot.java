package io.harborl.crawler.core;

import java.util.concurrent.TimeUnit;

/**
 * A robot is a abstract of task to submit a root link to crawl. <br/>
 * 
 * @author Harbor Luo
 * @since v0.0.1
 * @param <T> the result entity type
 * @param <V> the additional data type
 * 
 */
public interface Robot<T, V> {
  /** 
   * Start and wait the crawl process to be completed.<br/> 
   *  
   * @param timeout the maximum time to wait
   * @param unit the time unit of the timeout argument
   */
  Result<T, V> startAndAwait(long timeout, TimeUnit unit);

  /**
   * Start the crawl process asynchronously.
   * 
   * @param callback
   */
  void startAsync(Callback callback, Object tag);
}
