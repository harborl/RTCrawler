package io.harborl.crawler.coldwell.domain;

public class AgentList {
  
  private String title;
  private int count;
  private String link;
  private String referrUrl;
  
  public AgentList() { }

  public AgentList(String title, int count, String link, String referrUrl) {
    this.title = title;
    this.count = count;
    this.link = link;
    this.referrUrl = referrUrl;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }

  public String getLink() {
    return link;
  }

  public void setLink(String link) {
    this.link = link;
  }

  public String getReferrUrl() {
    return referrUrl;
  }

  public void setReferrUrl(String referrUrl) {
    this.referrUrl = referrUrl;
  }

  @Override
  public String toString() {
    return "AgentList [title=" + title + ", count=" + count + ", link=" + link + "]";
  }

}
