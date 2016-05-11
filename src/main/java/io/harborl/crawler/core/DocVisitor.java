package io.harborl.crawler.core;

import java.util.List;

/**
 * A visitor used to walked through the HTML document, then return the
 * collection of result and external meta data.
 * 
 * @author Harbor Luo
 * @since v0.0.1
 * 
 * @param <T> the production result type
 * @param <V> the production meta data type
 * @param <D> the HTML document type
 * 
 */
public interface DocVisitor<T, V, D> {
  /** Provides the functionality of visiting the overall document */
  public TwoTuple<List<T>, V> visit(D doc);
}
