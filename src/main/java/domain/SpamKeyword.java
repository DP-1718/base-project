
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
public class SpamKeyword extends DomainEntity {

	// Constructors -----------------------------------------------------------

	public SpamKeyword() {
		super();
	}


	// Attributes -------------------------------------------------------------
	private String	word;


	@NotBlank
	@Column(unique = true)
	public String getWord() {
		return this.word;
	}
	public void setWord(final String word) {
		this.word = word;
	}

}
