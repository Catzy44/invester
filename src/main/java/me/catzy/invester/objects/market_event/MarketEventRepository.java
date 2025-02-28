package me.catzy.invester.objects.market_event;

import java.sql.Timestamp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import me.catzy.invester.objects.market_event.MarketEventProcessingService.Estimation;

@RepositoryRestResource(collectionResourceRel = "market_events", path = "market_events")
public interface MarketEventRepository extends JpaRepository<MarketEvent, Long> {
	/*@Query("SELECT NEW me.catzy.invester.objects.market_event.MarketEventProcessingService$Estimation( " +
	           "SUM(CASE WHEN me.type = 1 THEN me.impact ELSE 0 END), " +
	           "SUM(CASE WHEN me.type = 0 THEN me.impact ELSE 0 END)) " +
	           "FROM MarketEvent me " +
	           "WHERE DATE(me.startTimestamp) = DATE(:day) " +
	           "GROUP BY DATE_FORMAT(me.startTimestamp, '%Y-%m-%d %H') " +
	           "ORDER BY DATE_FORMAT(me.startTimestamp, '%Y-%m-%d %H') ")*/
	@Query("SELECT NEW me.catzy.invester.objects.market_event.MarketEventProcessingService$Estimation( " +
		       "SUM(CASE WHEN me.type = 1 THEN me.impact ELSE 0 END), " +
		       "SUM(CASE WHEN me.type = 0 THEN me.impact ELSE 0 END)) " +
		       "FROM MarketEvent me " +
		       "WHERE DATE(me.startTimestamp) = DATE(:day) ")
	public Estimation getDailyEstimation(@Param("day") Timestamp day);
}
