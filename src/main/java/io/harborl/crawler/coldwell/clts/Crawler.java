package io.harborl.crawler.coldwell.clts;

import io.harborl.crawler.coldwell.AtomRobot;
import io.harborl.crawler.coldwell.Robots;
import io.harborl.crawler.coldwell.domain.Agent;
import io.harborl.crawler.core.Result;
import io.harborl.crawler.core.Stopwatch;
import io.harborl.crawler.core.Util;

import java.util.concurrent.TimeUnit;

public class Crawler {

  private static String root = "https://www.coldwellbankerhomes.com/sitemap/agents/";
  private static String domain = "https://www.coldwellbankerhomes.com";

  public static void main(String[] args) {
    Util.setupInsecureSSLContext();
    
    Stopwatch sw = new Stopwatch();
    try {
      Result<Agent, Void> result = 
          AtomRobot.valueOf(root, domain).startAndAwait(Integer.MAX_VALUE, TimeUnit.SECONDS);
      System.out.println("total: " + result.size() + " - " + sw.elapsedTime() + " S");
    } finally {
      Robots.shutdown();
    }
  }
}
