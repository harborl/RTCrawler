package io.harborl.crawler.coldwell;

import io.harborl.crawler.coldwell.domain.Agent;
import io.harborl.crawler.coldwell.domain.AgentList;
import io.harborl.crawler.core.AwaitableRobot;
import io.harborl.crawler.core.NamedCallable;
import io.harborl.crawler.core.Result;
import io.harborl.crawler.core.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class AtomRobot extends AwaitableRobot<Agent, Void> {

  private final String root;
  private final String domain;
  
  private AtomRobot(String root, String domain) {
    this.root = root;
    this.domain = domain;
  }
  
  public static AtomRobot valueOf(String root, String domain) {
    return new AtomRobot(root, domain);
  }
  
  @Override
  protected Callable<Result<Agent, Void>> rootTask() {
    return new Root(root, true);
  }

  @Override
  public String toString() {
    return "AtomRobot [root=" + root + ", domain=" + domain + "]";
  }

  class Root extends NamedCallable<Result<Agent, Void>> {
    private final String url;
    private final boolean isRoot;
    Root(String url, boolean isRoot) {
      super("[AtomRobot | Root] - %b - %s", isRoot, url);
      this.url = url;
      this.isRoot = isRoot;
    }

    @Override
    protected Result<Agent, Void> exec() {
      Result<AgentList, Void> result =
          new AgentListRobot(url, domain).startAndAwait(Integer.MAX_VALUE, TimeUnit.SECONDS);
      if (result == null)  throw new AssertionError("root results - " + isRoot);
      
      if (isRoot) {
        List<Root> workers = new ArrayList<Root>();
        for (AgentList ags : result.get()) {
          workers.add(new Root(ags.getLink(), false));
        }
        
        List<Agent> agents = new ArrayList<Agent>();
        List<Result<Agent, Void>> agentResults = AtomRobot.this.coInvokeAll(workers);
        for (Result<Agent, Void> ret : agentResults) {
          agents.addAll(ret.get());
        }
        
        return Util.newResult(agents, null);
      } else {
        List<Worker> workers = new ArrayList<Worker>();
        for (AgentList ags : result.get()) {
          workers.add(new Worker(ags));
        }
        
        List<Agent> agents = new ArrayList<Agent>();
        List<Result<Agent, Void>> agentResults = AtomRobot.this.coInvokeAll(workers);
        for (Result<Agent, Void> ret : agentResults) {
          agents.addAll(ret.get());
        }
        
        return Util.newResult(agents, null);
      }
    }
  }
  
  class Worker extends NamedCallable<Result<Agent, Void>> {
    private final AgentList agents;
    Worker(AgentList agents) {
      super("[AtomRobot | Worker] - %s", agents.getLink());
      this.agents = agents;
    }

    @Override
    protected Result<Agent, Void> exec() {
      return new AgentRobot(domain, agents).startAndAwait(Integer.MAX_VALUE, TimeUnit.SECONDS);
    }
  }
}
