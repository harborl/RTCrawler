package io.harborl.crawler.core;

/**
 * A HTMLable entity has the capacity of returning a 
 * HTML formated content.
 * <br/>
 * 
 * It used to restrain the generic type of {@linkplain HTMLResult}.
 * @author Harbor Luo
 * @since v0.0.1
 *
 */
public interface HTMLable {
  /** Returns the HTML formated content */
  String toHTML();
}
