
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
public class MessageFolder extends DomainEntity {

	// Constructors -----------------------------------------------------------

	public MessageFolder() {
		super();
	}


	// Attributes -------------------------------------------------------------

	private String	name;
	private Boolean	modificable;


	@NotBlank
	public String getName() {
		return this.name;
	}
	public void setName(final String name) {
		this.name = name;
	}

	public Boolean getModificable() {
		return this.modificable;
	}
	public void setModificable(final Boolean modificable) {
		this.modificable = modificable;
	}


	// Relationships ----------------------------------------------------------
	private Collection<Message>			messages;
	private Actor						actor;
	private MessageFolder				father;
	private Collection<MessageFolder>	childs;


	@Valid
	@NotNull
	@OneToMany(mappedBy = "messageFolder")
	@Cascade(CascadeType.DELETE)
	public Collection<Message> getMessages() {
		return this.messages;
	}
	public void setMessages(final Collection<Message> messages) {
		this.messages = messages;
	}

	@Valid
	@NotNull
	@ManyToOne(optional = false)
	public Actor getActor() {
		return this.actor;
	}

	public void setActor(final Actor actor) {
		this.actor = actor;
	}

	@NotNull
	@Valid
	@OneToMany(mappedBy = "parent")
	public Collection<MessageFolder> getChilds() {
		return this.childs;
	}
	public void setChilds(final Collection<MessageFolder> childs) {
		this.childs = childs;
	}

	@ManyToOne(optional = true)
	public MessageFolder getParent() {
		return this.father;
	}
	public void setParent(final MessageFolder parent) {
		this.father = parent;
	}

}
