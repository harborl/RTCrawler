package io.harborl.crawler.core;

import java.util.Map;

/**
 * An immutable, compatible with {@link Map#entrySet()} 
 * two value tuple class.
 * 
 * @author Harbor Luo
 * @since v0.0.1
 * @param <K> the key type or the first value
 * @param <V> the value type or the second value
 * 
 */
public class TwoTuple<K, V> implements Map.Entry<K, V> {
  private final K key;
  private final V value;

  private TwoTuple(K key, V value) {
    this.key = key;
    this.value = value;
  }
  
  /** Provides a static factory method to deduce the generic types */
  public static <K, V> TwoTuple<K, V> of(K key, V value) {
    return new TwoTuple<K, V>(key, value);
  }
  
  @Override
  public K getKey() {
    return key;
  }

  @Override
  public V getValue() {
    return value;
  }

  @Override
  public V setValue(V value) {
    throw new AssertionError("change disabled");
  }

}
