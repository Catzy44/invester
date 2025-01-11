package me.catzy.prestiz.objects.service.conspects.fields.replies;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;

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
@Table(name = "service_conspect_field_reply")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@JsonView({ConspectFieldReply.values.class})
public class ConspectFieldReply {
	public static interface id {}
	public static interface values extends id ,ConspectField.id,Instruktor.id{}
	
	@JsonView({id.class})
	@Id
	@Access(AccessType.PROPERTY)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name= "conspectFieldId")
	private ConspectField conspectField;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name= "instructorId")
	private Instruktor instructor;
	
	private String reply;
	
	private Timestamp timestamp;
}
