package io.harborl.crawler.core;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * A simple, {@code Jsoup} based HTML crawler.
 * The instance <strong>accept</strong> with both URL and HTML content, 
 * <ul>
 *   <li><strong>HTML:</strong> 
 *   the content will be visited directly.
 *   <li><strong>URL:</strong> 
 *   the content will be download through a HTTP request, and visited then.
 * </ul>
 * 
 * This crawler also <strong>accept</strong> 
 * a {@link DocVisitor} to walk through the HTML document, meanwhile.
 * 
 * @author Harbor Luo
 * @since v0.0.1
 *
 * @param <T> the entity type
 * 
 */
public class JsoupCrawler<T, V> implements Crawler<T, V, Document> {
  
  private final int tryCount;
  private JsoupCrawler(int tryCount) { 
    this.tryCount = tryCount;
  }
  
  public static <T, V> JsoupCrawler<T, V> of(int tryCount) {
    return new JsoupCrawler<T, V>(tryCount);
  }
  
  @Override
  public Result<T, V> acceptHTML(String html, DocVisitor<T, V, Document> visitor) {
    if (html == null || visitor == null) throw new NullPointerException();
    
    final Document doc = Jsoup.parse(html);
    TwoTuple<List<T>, V> results = visitor.visit(doc);
    if (results != null) {
      return Util.newResult(Collections.unmodifiableList(results.getKey()), results.getValue());
    } else {
      return null;
    }
  }
  
  @Override
  public Result<T, V> acceptURL(String url, DocVisitor<T, V, Document> visitor) {
    if (url == null || visitor == null) throw new NullPointerException();
    
    try {
      return accept(url, visitor);
    } catch (IOException rethrow) {
      // Retry
      for (int i = 0; i < tryCount; ++i) {
        try {
          return accept(url, visitor);
        } catch (IOException again) {
          rethrow = again;
        }
      }

      // Can not recovery from this network error,
      // just re-throw it and let program be 'fail-fast'.
      throw new RuntimeException(rethrow);
    }
  }

  private Result<T, V> accept(String url, DocVisitor<T, V, Document> visitor) throws IOException {
    Document doc = Jsoup.connect(url).timeout(1000 * 60 * 2)
        .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.109 Safari/537.36")
        .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
        .header("Accept-Encoding", "gzip, deflate, sdch")
        .header("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6")
        .get();
    
    TwoTuple<List<T>, V> results = visitor.visit(doc);
    if (results != null) {
      return Util.newResult(Collections.unmodifiableList(results.getKey()), results.getValue());
    } else {
      return null;
    }
  }
}
