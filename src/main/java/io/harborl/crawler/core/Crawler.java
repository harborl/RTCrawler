package io.harborl.crawler.core;

public interface Crawler<T, V, D> {
  public Result<T, V> acceptHTML(String html, DocVisitor<T, V, D> visitor);
  public Result<T, V> acceptURL(String url, DocVisitor<T, V, D> visitor);
}
