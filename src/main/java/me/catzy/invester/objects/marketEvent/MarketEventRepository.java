package me.catzy.invester.objects.marketEvent;

import java.sql.Timestamp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import me.catzy.invester.objects.marketEvent.MarketEventProcessingService.EstimationDTO;

@RepositoryRestResource(collectionResourceRel = "market_events", path = "market_events")
public interface MarketEventRepository extends JpaRepository<MarketEvent, Long> {
	
	@Query("SELECT NEW me.catzy.invester.objects.marketEvent.MarketEventProcessingService$EstimationDTO( " +
		       "SUM(CASE WHEN me.type = 1 THEN me.impact ELSE 0 END), " +
		       "SUM(CASE WHEN me.type = 0 THEN me.impact ELSE 0 END)) " +
		       "FROM MarketEvent me " +
		       "WHERE DATE(me.startTimestamp) = DATE(:day) ")
	public EstimationDTO getDailyEstimation(@Param("day") Timestamp day);
	
}
