package me.catzy.prestiz.objects.sms;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.sql.Timestamp;
import java.util.List;
import me.catzy.prestiz.objects.sms_group.SMSGroup;
import me.catzy.prestiz.objects.uczestnicy.Uczestnik;

@Entity
@Table(name = "sms")
@JsonView({SMS.vSMSRest.class})
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SMS {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  
  @ManyToOne
  @JoinColumn(name = "uczestnikId", nullable = true)
  @JsonView({vSMSUczestnik.class})
  private Uczestnik uczestnik;
  
  @Transient
  @JsonView({vSMSAttenders.class})
  private List<Uczestnik> uczestnicy;
  
  @Transient
  private Integer unreadSMSCountFromThisNumber;
  
  private int type;
  
  private int status;
  
  @Column(name = "read_status")
  @JsonView({vSMSReadStatus.class})
  private int readStatus;
  
  @JsonInclude(JsonInclude.Include.ALWAYS)
  private String content;
  
  private String number;
  
  @Column(name = "created_timestamp")
  private Timestamp createdTimestamp;
  
  @Column(name = "sent_timestamp")
  private Timestamp sentTimestamp;
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_sms_group", nullable = true)
  @JsonIgnore
  private SMSGroup SMSGroup;
  
  @Column(name = "fail_message")
  @JsonView({vSMSFailMessage.class})
  private String failMessage;
  
  public SMS(int id, int status, int read_status, String content, String number, Timestamp created_timestamp, Timestamp sent_timestamp, Integer unreadSMSCountFromThisNumber) {
    this.id = Integer.valueOf(id);
    this.status = status;
    this.readStatus = read_status;
    this.content = content;
    this.number = number;
    this.createdTimestamp = created_timestamp;
    this.sentTimestamp = sent_timestamp;
    this.unreadSMSCountFromThisNumber = unreadSMSCountFromThisNumber;
  }
  
  public void setUczestnik(Uczestnik ucz) {
    this.uczestnik = ucz;
  }
  
  protected SMS clone() {
    SMS copy = new SMS();
    copy.setContent(getContent());
    copy.setCreatedTimestamp(getCreatedTimestamp());
    copy.setFailMessage(getFailMessage());
    copy.setId(getId());
    copy.setNumber(getNumber());
    copy.setReadStatus(getReadStatus());
    copy.setSentTimestamp(getSentTimestamp());
    copy.setSMSGroup(getSMSGroup());
    copy.setStatus(getStatus());
    copy.setType(getType());
    copy.setUczestnicy(getUczestnicy());
    copy.setUczestnik(getUczestnik());
    copy.setUnreadSMSCountFromThisNumber(getUnreadSMSCountFromThisNumber());
    return copy;
  }
  
  public SMS() {}
  
  public Integer getId() {
    return this.id;
  }
  
  public Uczestnik getUczestnik() {
    return this.uczestnik;
  }
  
  public List<Uczestnik> getUczestnicy() {
    return this.uczestnicy;
  }
  
  public Integer getUnreadSMSCountFromThisNumber() {
    return this.unreadSMSCountFromThisNumber;
  }
  
  public int getType() {
    return this.type;
  }
  
  public int getStatus() {
    return this.status;
  }
  
  public int getReadStatus() {
    return this.readStatus;
  }
  
  public String getContent() {
    return this.content;
  }
  
  public String getNumber() {
    return this.number;
  }
  
  public Timestamp getCreatedTimestamp() {
    return this.createdTimestamp;
  }
  
  public Timestamp getSentTimestamp() {
    return this.sentTimestamp;
  }
  
  public SMSGroup getSMSGroup() {
    return this.SMSGroup;
  }
  
  public String getFailMessage() {
    return this.failMessage;
  }
  
  public void setId(Integer id) {
    this.id = id;
  }
  
  @JsonView({vSMSAttenders.class})
  public void setUczestnicy(List<Uczestnik> uczestnicy) {
    this.uczestnicy = uczestnicy;
  }
  
  public void setUnreadSMSCountFromThisNumber(Integer unreadSMSCountFromThisNumber) {
    this.unreadSMSCountFromThisNumber = unreadSMSCountFromThisNumber;
  }
  
  public void setType(int type) {
    this.type = type;
  }
  
  public void setStatus(int status) {
    this.status = status;
  }
  
  @JsonView({vSMSReadStatus.class})
  public void setReadStatus(int readStatus) {
    this.readStatus = readStatus;
  }
  
  public void setContent(String content) {
    this.content = content;
  }
  
  public void setNumber(String number) {
    this.number = number;
  }
  
  public void setCreatedTimestamp(Timestamp createdTimestamp) {
    this.createdTimestamp = createdTimestamp;
  }
  
  public void setSentTimestamp(Timestamp sentTimestamp) {
    this.sentTimestamp = sentTimestamp;
  }
  
  @JsonIgnore
  public void setSMSGroup(SMSGroup sMSGroup) {
    this.SMSGroup = sMSGroup;
  }
  
  @JsonView({vSMSFailMessage.class})
  public void setFailMessage(String failMessage) {
    this.failMessage = failMessage;
  }
  
  public class Type {
    public static final int OUT = 0;
    
    public static final int IN = 1;
  }
  
  public class Status {
    public static final int AWAITING = 0;
    
    public static final int SENT = 1;
    
    public static final int ERRORED = 2;
  }
  
  public static interface vSMSFull extends vSMSUczestnik, vSMSReadStatus, vSMSFailMessage, vSMSRest {}
  
  public static interface vSMSUczestnik {}
  
  public static interface vSMSReadStatus {}
  
  public static interface vSMSFailMessage {}
  
  public static interface vSMSRest {}
  
  public static interface vSMSAttenders {}
}
