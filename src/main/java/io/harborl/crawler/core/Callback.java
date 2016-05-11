package io.harborl.crawler.core;

/**
 * A callback used to injected to {@linkplain Robot#async(Callback)},
 * and it will be invoked when the robot operation completes or failures
 * 
 * @author Harbor Luo
 * @since v0.0.1
 * 
 */
public interface Callback {
  <T, V> void onCompleted(Result<T, V> results, Object tag);
  void onFailure(Exception e, Object tag);
}
