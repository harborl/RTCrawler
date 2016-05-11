package io.harborl.crawler.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


/**
 * An AwaitableRobot robot supports specified timeout interrupt.
 * <p/>
 * <strong>Two Points:</strong>
 * <ul>
 *   <li>Should override the {@code toString} to generate human-readable output.</li>
 *   <li>Should override the {@code rootTask} method to return the task need submitting.</li>
 * </ul>
 * 
 * It will cancel/interrupt the timeout task submitted before, so you
 * should consider to be responsible to the thread interrupt within the task calling.
 * 
 * @author Harbor Luo
 * @since v0.0.1
 *
 * @param <T> the result entity type
 * @param <V> the additional data type
 */
public abstract class AwaitableRobot<T, V> implements Robot<T, V> {
  
  @SuppressWarnings("unchecked")
  @Override public Result<T, V> startAndAwait(long timeout, TimeUnit unit) {
    Callable<Result<T, V>> task = rootTask();
    if (task == null) throw new AssertionError("root task should not be null.");
    Future<Result<T, V>> future = TaskExecutors.webCrawlerExecutor().submit(task);
    
    Result<T, V> result = null;
    try {
      result = future.get(timeout, unit);
    } catch (InterruptedException e) {
      // Somewhere interrupt current thread for some reasons.
      // Actually nothing can do here, just recovery the interrupt status
      // and cancel the work and returns a empty result and leave quickly .
      System.err.println("Crawl interrupted : " + this);
      Thread.currentThread().interrupt();
    } catch (ExecutionException e) {
      // The Worker throwed exception during its job.
      // Firstly, hand over your owner checked exception,
      // then, just rethrow the laundered root cause.
      Throwable cause = e.getCause();
      // Since there is no my owner checked exception need handing over,
      // so just rethrow the laundered root cause.
      throw Exceptions.launderThrowable(cause);
    } catch (TimeoutException e) {
      // The Worker has been timeout for it's job.
      // Just ignored this error, cancel task and returns a empty result
      System.err.println("Crawl time out - " + timeout + " " + unit + " : " + this);
    } finally {
      // For running task, cancel it.
      // For completed task, nothing happens.
      future.cancel(true);
    }
    
    // The type cast only happens when the result is null, which causes the warning suppress.
    return (Result<T, V>) (result == null ? Util.newResult(Collections.emptyList(), null) : result);
  }

  @Override
  public void startAsync(Callback callback, Object tag) {
    throw new AssertionError("not implemented!");
  }
  
  /** Invokes task collection to execute and wait to completed. it would be interrupted, 
   *  which has the same meaning with the ExecutorService invokeAll .*/
  public List<Result<T, V>> coInvokeAll(Collection<? extends Callable<Result<T, V>>> tasks) {
    if (tasks == null) throw new NullPointerException();
    List<Result<T, V>> results = new ArrayList<Result<T, V>>();
    try {
      // Returns from current job immediately,
      // if current job has been cancelled.
      if (Thread.currentThread().isInterrupted()) return null;
      
      List<Future<Result<T, V>>> futures = 
          TaskExecutors.webCrawlerExecutor().invokeAll(tasks);
      for (Future<Result<T, V>> f : futures) {
        try {
          results.add(f.get());
        } catch (ExecutionException e) {
          Throwable t = e.getCause();
          throw Exceptions.launderThrowable(t);
        }
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
    return results;
  }
  
  /** Push sub task to execute, it returns a Future latch used to wait the result. */
  public <S> Future<S> submit(Callable<S> task) {
    if (task == null) throw new NullPointerException();
    // Returns from current job immediately,
    // if current job has been cancelled.
    if (Thread.currentThread().isInterrupted()) return null;
    
    return TaskExecutors.webCrawlerExecutor().submit(task);
  }
  
  /** Provides the root task to be call within a timeout support strategy */
  protected abstract Callable<Result<T, V>> rootTask();
}
