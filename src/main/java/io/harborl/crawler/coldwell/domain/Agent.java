package io.harborl.crawler.coldwell.domain;

public class Agent {
  
  private String name;
  private String calbre;
  private String jobTitle;
  private String contact;
  private String email;
  private String mobilePhone;
  private String officePhone;
  private String directPhone;
  private String officeAddress;
  private String officeLink;
  private String intro;
  private String image;
  private String source;
  
  public Agent() { }

  public Agent(
      String name, String calbre, String jobTitle, String contact,
      String email, String mobilePhone, String officePhone,
      String directPhone, String officeAddress, String officeLink,
      String intro, String image, String source) {
    this.name = name;
    this.calbre = calbre;
    this.jobTitle = jobTitle;
    this.contact = contact;
    this.email = email;
    this.mobilePhone = mobilePhone;
    this.officePhone = officePhone;
    this.directPhone = directPhone;
    this.officeAddress = officeAddress;
    this.officeLink = officeLink;
    this.intro = intro;
    this.image = image;
    this.source = source;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getJobTitle() {
    return jobTitle;
  }

  public void setJobTitle(String jobTitle) {
    this.jobTitle = jobTitle;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getMobilePhone() {
    return mobilePhone;
  }

  public void setMobilePhone(String mobilePhone) {
    this.mobilePhone = mobilePhone;
  }

  public String getOfficePhone() {
    return officePhone;
  }

  public void setOfficePhone(String officePhone) {
    this.officePhone = officePhone;
  }

  public String getDirectPhone() {
    return directPhone;
  }

  public void setDirectPhone(String directPhone) {
    this.directPhone = directPhone;
  }

  public String getOfficeAddress() {
    return officeAddress;
  }

  public void setOfficeAddress(String officeAddress) {
    this.officeAddress = officeAddress;
  }

  public String getOfficeLink() {
    return officeLink;
  }

  public void setOfficeLink(String officeLink) {
    this.officeLink = officeLink;
  }

  public String getIntro() {
    return intro;
  }

  public void setIntro(String intro) {
    this.intro = intro;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }
  
  public String getContact() {
    return contact;
  }

  public void setContact(String contact) {
    this.contact = contact;
  }

  @Override
  public String toString() {
    return "Agent [name=" + name + ", contact=" + contact + ", calbre=" + calbre + ", jobTitle=" + jobTitle + ", source=" + source + "]";
  }

  public String getCalbre() {
    return calbre;
  }

  public void setCalbre(String calbre) {
    this.calbre = calbre;
  }

}
