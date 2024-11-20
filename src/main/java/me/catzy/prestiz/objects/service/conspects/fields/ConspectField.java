package me.catzy.prestiz.objects.service.conspects.fields;

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
import me.catzy.prestiz.objects.service.conspects.Conspect;

@Getter
@Setter
@Entity
@Table(name = "service_conspect")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class ConspectField {
	@Id
	@Access(AccessType.PROPERTY)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "conspectId")
	private Conspect conspect;
	
	private int type;
	private int sort;
	private String value;
}
