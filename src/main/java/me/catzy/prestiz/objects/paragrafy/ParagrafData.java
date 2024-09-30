package me.catzy.prestiz.objects.paragrafy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Arrays;
import me.catzy.prestiz.objects.grupy.Grupa;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ParagrafData {
  public String content = null;
  
  public String buttontext = null;
  
  public Integer group = null;
  
  private Grupa groupData = null;
  
  public Integer[] groups = null;
  
  private Grupa[] groupsData = null;
  
  public Boolean showhours = null;
  
  public Boolean required = null;
  
  public Boolean attach = null;
  
  public int price;
  
  public String getContent() {
    return this.content;
  }
  
  public String getButtontext() {
    return this.buttontext;
  }
  
  public Integer getGroup() {
    return this.group;
  }
  
  public Grupa getGroupData() {
    return this.groupData;
  }
  
  public Integer[] getGroups() {
    return this.groups;
  }
  
  public Grupa[] getGroupsData() {
    return this.groupsData;
  }
  
  public Boolean getShowhours() {
    return this.showhours;
  }
  
  public Boolean getRequired() {
    return this.required;
  }
  
  public Boolean getAttach() {
    return this.attach;
  }
  
  public int getPrice() {
    return this.price;
  }
  
  public void setContent(String content) {
    this.content = content;
  }
  
  public void setButtontext(String buttontext) {
    this.buttontext = buttontext;
  }
  
  public void setGroup(Integer group) {
    this.group = group;
  }
  
  public void setGroupData(Grupa groupData) {
    this.groupData = groupData;
  }
  
  public void setGroups(Integer[] groups) {
    this.groups = groups;
  }
  
  public void setGroupsData(Grupa[] groupsData) {
    this.groupsData = groupsData;
  }
  
  public void setShowhours(Boolean showhours) {
    this.showhours = showhours;
  }
  
  public void setRequired(Boolean required) {
    this.required = required;
  }
  
  public void setAttach(Boolean attach) {
    this.attach = attach;
  }
  
  public void setPrice(int price) {
    this.price = price;
  }
  
  public boolean equals(Object o) {
    if (o == this)
      return true; 
    if (!(o instanceof ParagrafData))
      return false; 
    ParagrafData other = (ParagrafData)o;
    if (!other.canEqual(this))
      return false; 
    if (getPrice() != other.getPrice())
      return false; 
    Object this$group = getGroup();
    Object other$group = other.getGroup();
    if ((this$group == null) ? (
      other$group == null) : 
      
      this$group.equals(other$group)) {
      Object this$showhours = getShowhours();
      Object other$showhours = other.getShowhours();
      if ((this$showhours == null) ? (
        other$showhours == null) : 
        
        this$showhours.equals(other$showhours)) {
        Object this$required = getRequired();
        Object other$required = other.getRequired();
        if ((this$required == null) ? (
          other$required == null) : 
          
          this$required.equals(other$required)) {
          Object this$attach = getAttach();
          Object other$attach = other.getAttach();
          if ((this$attach == null) ? (
            other$attach == null) : 
            
            this$attach.equals(other$attach)) {
            Object this$content = getContent();
            Object other$content = other.getContent();
            if ((this$content == null) ? (
              other$content == null) : 
              
              this$content.equals(other$content)) {
              Object this$buttontext = getButtontext();
              Object other$buttontext = other.getButtontext();
              if ((this$buttontext == null) ? (
                other$buttontext == null) : 
                
                this$buttontext.equals(other$buttontext)) {
                Object this$groupData = getGroupData();
                Object other$groupData = other.getGroupData();
                if (this$groupData == null) {
                  if (other$groupData == null)
                    return (Arrays.deepEquals((Object[])getGroups(), (Object[])other.getGroups()) && Arrays.deepEquals((Object[])getGroupsData(), (Object[])other.getGroupsData())); 
                } else if (this$groupData.equals(other$groupData)) {
                  return (Arrays.deepEquals((Object[])getGroups(), (Object[])other.getGroups()) && Arrays.deepEquals((Object[])getGroupsData(), (Object[])other.getGroupsData()));
                } 
                return false;
              } 
              return false;
            } 
            return false;
          } 
          return false;
        } 
        return false;
      } 
      return false;
    } 
    return false;
  }
  
  protected boolean canEqual(Object other) {
    return other instanceof ParagrafData;
  }
  
  public int hashCode() {
    int PRIME = 59;
    int result = 1;
    result = result * 59 + getPrice();
    Object $group = getGroup();
    result = result * 59 + (($group == null) ? 43 : $group.hashCode());
    Object $showhours = getShowhours();
    result = result * 59 + (($showhours == null) ? 43 : $showhours.hashCode());
    Object $required = getRequired();
    result = result * 59 + (($required == null) ? 43 : $required.hashCode());
    Object $attach = getAttach();
    result = result * 59 + (($attach == null) ? 43 : $attach.hashCode());
    Object $content = getContent();
    result = result * 59 + (($content == null) ? 43 : $content.hashCode());
    Object $buttontext = getButtontext();
    result = result * 59 + (($buttontext == null) ? 43 : $buttontext.hashCode());
    Object $groupData = getGroupData();
    result = result * 59 + (($groupData == null) ? 43 : $groupData.hashCode());
    result = result * 59 + Arrays.deepHashCode((Object[])getGroups());
    result = result * 59 + Arrays.deepHashCode((Object[])getGroupsData());
    return result;
  }
  
  public String toString() {
    return "ParagrafData(content=" + getContent() + ", buttontext=" + getButtontext() + ", group=" + getGroup() + ", groupData=" + getGroupData() + ", groups=" + Arrays.deepToString((Object[])getGroups()) + ", groupsData=" + Arrays.deepToString((Object[])getGroupsData()) + ", showhours=" + getShowhours() + ", required=" + getRequired() + ", attach=" + getAttach() + ", price=" + getPrice();
  }
}
