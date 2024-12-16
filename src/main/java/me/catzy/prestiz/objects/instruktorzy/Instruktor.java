package me.catzy.prestiz.objects.instruktorzy;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import me.catzy.prestiz.objects.service.conspect.category.ConspectCategory;

@Entity
@Table(name = "Instruktorzy")
@JsonIgnoreProperties({ "haslo" })
@Getter
@Setter
@JsonView({Instruktor.values.class})
public class Instruktor {
	public interface values extends id {}

	public static interface id {}
	@JsonView({ id.class })
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "additionalInfo")
	private String additionalInfo;
	private String timestamp;
	private Integer idRoku;
	private boolean deleted;
	private String avatar;
	@Column(name = "imie")
	private String firstName;
	@Column(name = "nazwisko")
	private String lastName;
	@Column(name = "telefon")
	private String phoneNum;
	@Column(name = "nick")
	private String nick;
	@Column(name = "haslo")
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;
	@Column(name = "rodzaj")
	private int type;
	@Column(name = "idAktywnegoMiejsca")
	private Integer activePlaceId;
	@Column(name = "idAktywnejSpolki")
	private Integer activeCompanyId;
	private Integer sort;
	private String accesableYears;
	
	private boolean isAdmin;
	private boolean isHidden;
	private boolean showService;
	
	public interface allowedServiceCategories {}
	@JsonView({allowedServiceCategories.class})
	//@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
			  name = "service_conspect_category_instructor_allowed", 
			  joinColumns = @JoinColumn(name = "instructorId"), 
			  inverseJoinColumns = @JoinColumn(name = "categoryId"))
	private List<ConspectCategory> allowedServiceCategories;
}
