package io.harborl.crawler.coldwell;

import io.harborl.crawler.coldwell.domain.Agent;
import io.harborl.crawler.coldwell.domain.AgentList;
import io.harborl.crawler.core.AwaitableRobot;
import io.harborl.crawler.core.DocVisitor;
import io.harborl.crawler.core.JsoupCrawler;
import io.harborl.crawler.core.NamedCallable;
import io.harborl.crawler.core.Result;
import io.harborl.crawler.core.TwoTuple;
import io.harborl.crawler.core.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class AgentRobot extends AwaitableRobot<Agent, Void> {
  
  private final String domain;
  private final AgentList agents;
  private final static double perPage = 25.0; // according the fixed page property

  public AgentRobot(String domain, AgentList agents) {
    this.domain = domain;
    this.agents = agents;
  }

  @Override
  protected Callable<Result<Agent, Void>> rootTask() {
    return new Worker("[Agent | Worker] - %s", agents.getLink());
  }

  @Override
  public String toString() {
    return "AgentRobot [domain=" + domain + ", agents=" + agents + "]";
  }

  // "Ahwatukee real estate agents" "https://www.coldwellbankerhomes.com/az/ahwatukee/agents/" 61
  class Worker extends NamedCallable<Result<Agent, Void>> {

    public Worker(String format, Object... args) {
      super(format, args);
    }

    @Override
    protected Result<Agent, Void> exec() {
      final int totalCount = agents.getCount();
      final int pages = (int) Math.ceil(perPage/totalCount);
      
      List<SubWorker> subWorks = new ArrayList<SubWorker>(totalCount);
      for (int i = 1; i <= pages; i++) {
        String url = agents.getLink().trim();
        String suffix = url.charAt(url.length() - 1) == '/' ? "" : "/";
        suffix = suffix + "p_" + i + "/";
        String suburl = url + suffix;
        subWorks.add(new SubWorker(suburl));
      }

      List<Agent> results = new ArrayList<Agent>();
      List<Result<Agent, Void>> segments = AgentRobot.this.coInvokeAll(subWorks);
      for (Result<Agent, Void> s : segments) {
        results.addAll(s.get());
      }
      
      return Util.newResult(Collections.unmodifiableList(results), null);
    }
    
    // https://www.coldwellbankerhomes.com/az/ahwatukee/agents/
    class SubWorker extends NamedCallable<Result<Agent, Void>> {
      private final String url;

      public SubWorker(String url) {
        super("[Agent | SubWork] - %s", url);
        this.url = url;
      }

      @Override
      protected Result<Agent, Void> exec() {
        return JsoupCrawler.<Agent, Void>of(5).acceptURL(url, new DocVisitor<Agent, Void, Document>() {

          @Override
          public TwoTuple<List<Agent>, Void> visit(Document doc) {
            List<Agent> results = new ArrayList<Agent>();
            Elements agentcols = doc.select(".agent-block").select(".col");
            if (agentcols != null && agentcols.size() > 0) {
              
              List<AgentWorker> works = new ArrayList<AgentWorker>(agentcols.size());
              for (Element col : agentcols) {
                Element e = col.select(".title").select(".notranslate").get(0);
                String agenturl = domain + e.getElementsByTag("a").get(0).attr("href");
                String name = e.text();
                
                works.add(new AgentWorker(agenturl, name));
              }

              List<Result<Agent, Void>> segments = AgentRobot.this.coInvokeAll(works);
              for (Result<Agent, Void> s : segments) {
                results.addAll(s.get());
              }
            }

            return TwoTuple.of(results, null);
          }
        });
      }
      
      // https://www.coldwellbankerhomes.com/az/tempe/agent/julia-julie-coast/aid_47808/
      class AgentWorker extends NamedCallable<Result<Agent, Void>> {
        private final String agentUrl;
        private final String name;

        public AgentWorker(String url, String name) {
          super("[Agent | AgentWork] - %s", url);
          this.agentUrl = url;
          this.name = name;
        }

        @Override
        protected Result<Agent, Void> exec() {
          return JsoupCrawler.<Agent, Void>of(5).acceptURL(agentUrl, new DocVisitor<Agent, Void, Document>() {

            @Override
            public TwoTuple<List<Agent>, Void> visit(Document doc) {
              List<Agent> results = new ArrayList<Agent>();
              Agent agent = new Agent();
              agent.setSource(agentUrl);
              agent.setName(name);

              Element profile = doc.select(".layout-main").select(".agent-profile").get(0);
              Elements cals = profile.select(".calbre");
              if (cals != null && cals.size() > 0) {
                String calbre = cals.get(0).text();
                agent.setCalbre(calbre);
              }
              
              Elements h2 = profile.select(".inner-wrap-mobile").get(0).getElementsByTag("h2");
              if (h2 != null && h2.size() > 0) {
                String jobTitle = h2.text();
                agent.setJobTitle(jobTitle);
              }

              Elements es = profile.select(".body");
              String contact = es.get(0).text();
              agent.setContact(contact);

              results.add(agent);
              System.out.println(agent);
              return TwoTuple.of(results, null);
            }
            
          });
        }
      }
    }
  }
}
