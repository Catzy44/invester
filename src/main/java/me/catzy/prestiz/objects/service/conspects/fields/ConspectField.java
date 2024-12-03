package me.catzy.prestiz.objects.service.conspects.fields;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

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
@Table(name = "service_conspect_field")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@JsonView(ConspectField.values.class)
public class ConspectField {
	public interface values{}
	
	@Id
	@Access(AccessType.PROPERTY)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@JsonIdentityReference(alwaysAsId = true)
	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "conspectId")
	private Conspect conspect;
	
	private int type;
	private int sort;
	private String value;
	
	private boolean deleted;
	private boolean visible;
}
