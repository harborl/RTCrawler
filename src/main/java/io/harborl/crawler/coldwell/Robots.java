package io.harborl.crawler.coldwell;

import io.harborl.crawler.core.TaskExecutors;

import java.util.concurrent.TimeUnit;

public class Robots {
  private Robots() { }
  
  public static void shutdown() {
    TaskExecutors.webCrawlerExecutor().shutdown();
    try {
      TaskExecutors.webCrawlerExecutor().awaitTermination(Integer.MAX_VALUE, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }

}
