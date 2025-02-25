package me.catzy.invester.objects.service.article;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "article")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@JsonView({Article.values.class})
public class Article {
	public interface values  extends id{}
	
	public static interface id {}
	@JsonView({id.class})
	@Id
	@Access(AccessType.PROPERTY)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	public String url;
	public String title;
	public String content;
	public Timestamp timestamp;
	
	public Integer impactGood;
	public Integer impactBad;
	public String scream;
}
