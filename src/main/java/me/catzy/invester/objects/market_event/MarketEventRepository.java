package me.catzy.invester.objects.market_event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "market_events", path = "market_events")
public interface MarketEventRepository extends JpaRepository<MarketEvent, Long> {
}
