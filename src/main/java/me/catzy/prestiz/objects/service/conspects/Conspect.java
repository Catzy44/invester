package me.catzy.prestiz.objects.service.conspects;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import me.catzy.prestiz.objects.service.conspects.Conspect.ConspectFull;
import me.catzy.prestiz.objects.service.conspects.fields.ConspectField;

@Getter
@Setter
@Entity
@Table(name = "service_conspect")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@JsonView({ConspectFull.class})
public class Conspect {
	
	public interface ConspectFull {}
	public interface ConspectFields {}
	
	
	@Id
	@Access(AccessType.PROPERTY)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column(name = "title")
	private String title;
	
	@JsonView({ConspectFields.class})
	@OneToMany(mappedBy="conspect")
	private List<ConspectField> fields;
}
