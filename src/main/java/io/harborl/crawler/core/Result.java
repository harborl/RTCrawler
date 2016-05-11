package io.harborl.crawler.core;

import java.util.List;

/**
 * The operation result returns by the {@linkplain Robot#startAndAwait(long, java.util.concurrent.TimeUnit)}
 * call. <br/>
 * 
 * <h4>NOTE</h4>
 * You can customize or decorate the result through the same way of
 * {@linkplain HTMLResult}.

 * @author Harbor Luo
 * @since v0.0.1
 * @param <T>  the result entity type
 * @param <V>  the external data type
 * 
 */
public interface Result<T, V> {
  /** Returns the size of this result */
  int size();
  
  /** Returns all results without any filter test */
  List<T> get();
  
  /** Returns the qualified results with a filter test */
  List<T> get(Filter<T> filter);
  
  /** Returns the external data besides the productions, 
   * or null if not exists */
  V ext();
}
