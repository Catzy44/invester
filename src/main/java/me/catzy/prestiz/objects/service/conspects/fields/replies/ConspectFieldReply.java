package me.catzy.prestiz.objects.service.conspects.fields.replies;

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
import me.catzy.prestiz.objects.service.conspects.fields.ConspectField;

@Getter
@Setter
@Entity
@Table(name = "service_conspects_field_reply")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class ConspectFieldReply {
	@Id
	@Access(AccessType.PROPERTY)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name= "conspectFieldId")
	private ConspectField conspectField;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name= "instructorId")
	private Instruktor instructor;
	
	private String reply;
}
