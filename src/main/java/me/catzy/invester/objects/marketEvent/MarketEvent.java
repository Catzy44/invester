package me.catzy.invester.objects.marketEvent;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class MarketEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int type; // 0 - negatywny wpływ, 1 - pozytywny
    private int impact; // Waga wpływu (0-10)
    @Column(name="start_timestamp")
    private Timestamp startTimestamp; // Timestamp początku wpływu
    @Column(name="end_timestamp")
    private Timestamp endTimestamp; // Timestamp końca wpływu
    private String scream; // Krótkie wyrazy: "Yield Drop", "Semiconductor Ban" itp.
    
    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article;
}