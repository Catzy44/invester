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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import me.catzy.prestiz.objects.service.conspect.category.ConspectCategory;
import me.catzy.prestiz.objects.service.conspects.fields.ConspectField;

@Getter
@Setter
@Entity
@Table(name = "service_conspect")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@JsonView({Conspect.values.class})
public class Conspect {
	public interface values  extends id{}
	
	public static interface id {}
	@JsonView({id.class})
	@Id
	@Access(AccessType.PROPERTY)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "title")
	private String title;
	
	private boolean deleted;
	
	public interface conspectFieldList {}
	@JsonView({conspectFieldList.class})
	@OneToMany(mappedBy="conspect")
	//@JsonIgnore
	@OrderBy("sort ASC")
	private List<ConspectField> fields;
	
	/*@JsonIdentityReference(alwaysAsId = true)
	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")*/
	public interface conspectCategoryList {}
	@JsonView({conspectCategoryList.class})
	@ManyToMany
	@JoinTable(
			  name = "service_conspect_in_category", 
			  joinColumns = @JoinColumn(name = "conspectId"), 
			  inverseJoinColumns = @JoinColumn(name = "categoryId"))
	private List<ConspectCategory> categories;
}
