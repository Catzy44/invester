package me.catzy.prestiz.objects.service.conspects.fields;

import java.util.List;

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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.catzy.prestiz.objects.service.conspects.Conspect;
import me.catzy.prestiz.objects.service.conspects.fields.replies.ConspectFieldReply;

@Getter
@Setter
@Entity
@Table(name = "service_conspect_field")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@JsonView(ConspectField.values.class)
@NoArgsConstructor
@AllArgsConstructor
public class ConspectField {
	public interface values extends id{}
	
	public static class Type {
		public static final int VIDEO = 1;
		public static final int TEXT = 2;
		public static final int DOWNLOAD = 3;
		public static final int QNA = 4;
	}
	
	public interface id {}
	@JsonView({id.class})
	@Id
	@Access(AccessType.PROPERTY)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	public interface conspect {}
	@JsonView({conspect.class})
	//@JsonIdentityReference(alwaysAsId = true)
	//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "conspectId")
	private Conspect conspect;
	
	public interface conspectFieldReplyList {}
	@JsonView({conspectFieldReplyList.class})
	@OneToMany(mappedBy = "conspectField")
	private List<ConspectFieldReply> conspectFieldReply;
	
	private int type;
	private int sort;
	private String value;
	
	private boolean deleted;
	private boolean visible;
}
