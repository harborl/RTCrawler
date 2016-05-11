package io.harborl.crawler.core;

import java.util.List;

/**
 * A <code>HTMLResult</code> is a decorator used to add functionality of 
 * returning a HTML view result with another underlying {@linkplain Result}.
 * 
 * @author Harbor Luo
 * @since v0.0.1
 * 
 * @param <T> the {@linkplain HTMLable} {@linkplain Result} type
 * 
 */
public class HTMLResult<T extends HTMLable, V> implements Result<T, V> {
  private final Result<T, V> ret;
  
  public HTMLResult(Result<T, V> ret) {
    this.ret = ret;
  }

  @Override
  public int size() {
    return ret.size();
  }

  @Override
  public List<T> get() {
    return ret.get();
  }

  @Override
  public List<T> get(Filter<T> filter) {
    return ret.get(filter);
  }

  /**
   * Returns a HTML view of the results.
   * 
   * @param title the title of the HTML view
   * @param filter used to filters the results
   * @return
   */
  public TwoTuple<Integer, String> html(String title, Filter<T> filter) {
    String HTMLStart= 
        "<!DOCTYPE html>"
        + "<html>"
        + "<head>"
        + "<title>" + title + "</title>"
        + "</head>"
        + "<body>";
    
    int count = 0;
    StringBuilder sb = new StringBuilder(HTMLStart);
    for (T p : ret.get()) {
      if (filter == null || filter.test(p)) {
        sb.append(p.toHTML() + "<p/>");
        ++count;
      }
    }
    String HTMLEnd= "</body>" + "</html>";
    sb.append(HTMLEnd);

    return TwoTuple.of(count, sb.toString());
  }

  @Override
  public V ext() {
    return ret.ext();
  }
}
