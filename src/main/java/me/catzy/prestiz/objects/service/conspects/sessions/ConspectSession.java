package me.catzy.prestiz.objects.service.conspects.sessions;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
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
import me.catzy.prestiz.objects.instruktorzy.Instruktor;
import me.catzy.prestiz.objects.service.conspects.Conspect;

@Getter
@Setter
@Entity
@Table(name = "service_conspect_session")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class ConspectSession {
	@Id
	@Access(AccessType.PROPERTY)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "conspectId")
	private Conspect conspect;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "instructorId")
	private Instruktor instructor;
	
	private Timestamp startTimestamp;
	
	private Timestamp endTimestamp;
}
