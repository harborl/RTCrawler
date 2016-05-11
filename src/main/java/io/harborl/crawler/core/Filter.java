package io.harborl.crawler.core;

/**
 * A filter used to test if the term qualifies the request <br/>
 * 
 * @author Harbor Luo
 * @since v0.0.1
 *
 * @param <T> the term type
 * 
 */
public interface Filter<T> {
  boolean test(T t);
}
