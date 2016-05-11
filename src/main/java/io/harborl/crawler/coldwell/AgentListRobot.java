package io.harborl.crawler.coldwell;

import io.harborl.crawler.coldwell.domain.AgentList;
import io.harborl.crawler.core.AwaitableRobot;
import io.harborl.crawler.core.DocVisitor;
import io.harborl.crawler.core.JsoupCrawler;
import io.harborl.crawler.core.NamedCallable;
import io.harborl.crawler.core.Result;
import io.harborl.crawler.core.TwoTuple;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class AgentListRobot extends AwaitableRobot<AgentList, Void> {
  
  private final String url;
  private final String domain;
  
  public AgentListRobot(String url, String domain) {
    this.url = url;
    this.domain = domain;
  }

  @Override
  protected Callable<Result<AgentList, Void>> rootTask() {
    return new Worker("[AgentListRobot] - %s", url);
  }
  
  @Override
  public String toString() {
    return "AgentListRobot [url=" + url + ", domain=" + domain + "]";
  }

  // https://www.coldwellbankerhomes.com/sitemap/agents/
  // https://www.coldwellbankerhomes.com/sitemap/arizona-agents/
  class Worker extends NamedCallable<Result<AgentList, Void>> {

    public Worker(String format, Object... args) {
      super(format, args);
    }

    @Override
    protected Result<AgentList, Void> exec() {
      return JsoupCrawler.<AgentList, Void>of(5).acceptURL(url, new DocVisitor<AgentList, Void, Document>() {
        @Override 
        public TwoTuple<List<AgentList>, Void> visit(Document doc) {
          List<AgentList> agentsList = new ArrayList<AgentList>();
          Elements content = doc.select(".layout-main").select(".table-sort").select(".notranslate");
          Elements trs = content.get(0).getElementsByTag("tr");
          for (Element tr : trs) {
            AgentList agents = newAgents(tr);
            agentsList.add(agents);
          }
          return TwoTuple.of(agentsList, null);
        }

        private AgentList newAgents(Element tr) {
          Elements tds = tr.getElementsByTag("td");
          String link = tds.get(0).getElementsByTag("a").attr("href");
          link = domain + link;
          String title = tds.get(0).getElementsByTag("a").text();
          int count = Integer.valueOf(tds.get(1).text().replace(",", ""));
          return new AgentList(title, count, link, url);
        }
        
      });
    }
  }


}
