package me.catzy.prestiz.objects.service.conspect.category;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import me.catzy.prestiz.objects.instruktorzy.Instruktor;
import me.catzy.prestiz.objects.service.conspects.Conspect;

@Getter
@Setter
@Entity
@Table(name = "service_conspect_category")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@JsonView({ConspectCategory.values.class})
public class ConspectCategory {
	public interface id {}
	public interface values extends id{}
	
	@JsonView({ConspectCategory.id.class})
	@Id
	@Access(AccessType.PROPERTY)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "title")
	private String title;
	
	@ManyToMany(mappedBy = "categories")
	@JsonIgnore
	private List<Conspect> conspects;
	
	@ManyToMany(mappedBy = "allowedServiceCategories")
	@JsonIgnore
	private List<Instruktor> instructors;
}
