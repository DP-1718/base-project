
package domain;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;

import utilities.Helpers;

@Entity
@Access(AccessType.PROPERTY)
public class Configuration extends DomainEntity {

	private String name;
	private String value;
	private String type;
	private Collection<String> validations;

	public Configuration() {
		super();
	}

	@NotBlank
	@Column(unique = true)
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@NotBlank
	public String getValue() {
		return this.value;
	}

	public void setValue(final String value) {
		this.value = value;
	}

	@NotBlank
	@Pattern(regexp = "^(String|Integer|Double|Date|Time|DateTime|Boolean|Array\\|(String|Integer|Double|Date|Time|DateTime|Boolean)\\|)$")
	public String getType() {
		return this.type;
	}

	public void setType(final String type) {
		this.type = type;
	}

	@NotNull
	@ElementCollection
	public Collection<String> getValidations() {
		return this.validations;
	}

	public void setValidations(final Collection<String> validations) {
		this.validations = validations;
	}

	public Object toRealType() throws NumberFormatException, ParseException {
		Object realTypeValue;

		String subType;
		String[] items;
		Collection<Object> list;

		realTypeValue = null;

		if (this.getType().startsWith("Array")) {
			subType = this.getType().substring(6, this.getType().length() - 1);
			items = this.getValue().split(";");
			list = new ArrayList<>();

			for (final String item : items) {
				list.add(Helpers.typeFromString(subType, item));
			}

			realTypeValue = list;
		} else {
			realTypeValue = Helpers.typeFromString(this.getType(), this.getValue());
		}

		return realTypeValue;
	}

}
