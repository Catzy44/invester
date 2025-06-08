package me.catzy.invester.objects.marketEvent;

import java.sql.Timestamp;

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
import lombok.Getter;
import lombok.Setter;
import me.catzy.invester.objects.article.Article;

@Entity
@Table(name = "market_events")
@Getter
@Setter
@JsonView({me.catzy.invester.objects.marketEvent.MarketEvent.values.class})
public class MarketEvent {
	public interface values {}
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int type; // 0 - negatywny wpływ, 1 - pozytywny
    private int impactPrc; // Waga wpływu (0-10)
    private int impactChance; // Waga wpływu (0-10)
    @Column(name="start_timestamp")
    private Timestamp startTimestamp; // Timestamp początku wpływu
    @Column(name="end_timestamp")
    private Timestamp endTimestamp; // Timestamp końca wpływu
    private String scream; // Krótkie wyrazy: "Yield Drop", "Semiconductor Ban" itp.
    
    public interface vArticle {}
    @JsonView({vArticle.class})
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article;
    
    @Override
    public String toString() {
    	return "type:"+(type == 0 ? "UP" : "DOWN")+",impactPrc:"+impactPrc+"impactChancePrc:"+impactChance+",startTtimestamp:"+startTimestamp.toString()+",endTimestamp:"+endTimestamp.toString()+",scream:"+scream+"\n";
    }
}