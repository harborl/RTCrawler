package io.harborl.crawler.core;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/** 
 * A utility class used to create suitable {@linkplain ExecutorService}
 * for robot's concurrent operation strategy.
 * 
 * @author Harbor Luo
 * @since v0.0.1
 *
 */
public final class TaskExecutors {
  private TaskExecutors() { }
  
  /**
   * <h4>Solution one: unbounded thread-pool</h4>
   * <p/>
   * Since the job depends the sub-job to be completed,
   * so we can use the unbounded thread-pool as the work executor.
   * DO NOT Change this configuration, unless you have reasonable requires.
   * <p/>
   * For this configurations, we referred [JCIP 8.3.2/3]. <br/>
   */
  private final static ExecutorService UNBOUNDED;
  static {
    UNBOUNDED = Executors.unconfigurableExecutorService(Executors.newCachedThreadPool(
      new ThreadFactory() {
        @Override public Thread newThread(Runnable runnable) {
          Thread result = new Thread(runnable, "Crawler Robot");
          result.setDaemon(false);
          return result;
        }
    }));
  }
  
  /**
   * <h4>Solution two: bounded thread-pool</h4>
   * <p/>
   * Since the job depends the sub-job to be completed,
   * so we can use a unbounded thread-pool with a SynchronousQueue and 
   * Caller-Runs Reject handler as the work executor.
   * DO NOT Change this configuration, unless you have reasonable requires.
   * <p/>
   * For this configurations, we referred [JCIP 8.3.2/3]. <br/>
   */
  private final static ExecutorService BOUNDED; 
  static {
    ThreadPoolExecutor swap = new ThreadPoolExecutor(0, Runtime.getRuntime().availableProcessors() * 2 + 1,
        60L, TimeUnit.SECONDS,
        new SynchronousQueue<Runnable>(),
        new ThreadFactory() {
          @Override public Thread newThread(Runnable runnable) {
            Thread result = new Thread(runnable, "Crawler Robot");
            result.setDaemon(false);
            return result;
          }
      });
    swap.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
    BOUNDED = Executors.unconfigurableExecutorService(swap);
  }

  static ExecutorService unboundedExecutor() {
    return UNBOUNDED;
  }
  
  static ExecutorService boundedExecutor() {
    return BOUNDED;
  }
  
  public static ExecutorService webCrawlerExecutor() {
    return BOUNDED;
  }
  
  static ExecutorService bizExecutor() {
    return UNBOUNDED;
  }
}
