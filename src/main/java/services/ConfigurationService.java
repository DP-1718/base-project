package services;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import domain.Configuration;
import repositories.ConfigurationRepository;
import utilities.Helpers;

@Service
@Transactional
public class ConfigurationService {

	@Autowired
	private ConfigurationRepository configurationRepository;

	@Autowired
	private AdministratorService administratorService;

	@Autowired
	private Validator validator;

	public ConfigurationService() {
		super();
	}

	public Collection<Configuration> findAll() {
		Collection<Configuration> items;

		items = this.configurationRepository.findAll();
		Assert.notNull(items);

		return items;
	}

	private Configuration findOne(int id) {
		Configuration item;

		Assert.notNull(id);
		item = this.configurationRepository.findOne(id);
		Assert.notNull(item);

		return item;
	}

	public Configuration findOneToEdit(int id) {
		Configuration item;

		Assert.isTrue(this.administratorService.isAdministratorLogged());
		Assert.notNull(id);
		item = this.findOne(id);
		Assert.notNull(item);

		return item;
	}

	public Configuration save(Configuration configuration) {
		Configuration configurationSaved;

		Assert.isTrue(this.administratorService.isAdministratorLogged());
		Assert.notNull(configuration);
		Assert.isTrue(this.configurationRepository.exists(configuration.getId()));
		configurationSaved = this.configurationRepository.save(configuration);
		Assert.notNull(configurationSaved);

		return configurationSaved;
	}

	public Configuration getEntryByName(String name) {
		Configuration configuration;

		Assert.notNull(name);
		configuration = this.configurationRepository.findByName(name);
		Assert.notNull(name);

		return configuration;
	}

	public Configuration reconstruct(Configuration configuration, BindingResult binding) {
		Configuration configurationReconstructed;
		Collection<String> messageParams;

		configurationReconstructed = this.findOne(configuration.getId());

		configurationReconstructed.setValue(configuration.getValue().replaceAll("\n", "").trim());
		if (configurationReconstructed.getType().equals("Boolean")) {
			configurationReconstructed
					.setValue(new Boolean(configurationReconstructed.getValue().replaceAll("1", "true")).toString());
		}

		try {
			configurationReconstructed.toRealType();

			if (!configurationReconstructed.getValidations().isEmpty()) {
				this.validateValue(configurationReconstructed.toRealType(), configurationReconstructed.getType(),
						configurationReconstructed.getValidations(), binding);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
			messageParams = new ArrayList<String>();
			messageParams.add(configuration.getType());
			binding.rejectValue("value", "common.error.parse", messageParams.toArray(), "");
		} catch (ParseException e) {
			e.printStackTrace();
			messageParams = new ArrayList<String>();
			messageParams.add(configuration.getType());
			binding.rejectValue("value", "common.error.parse", messageParams.toArray(), "");
		}

		// binding.rejectValue("value",
		// "org.hibernate.validator.constraints.URL.message");

		this.validator.validate(configurationReconstructed, binding);

		Assert.isTrue(!binding.hasErrors());

		return configurationReconstructed;
	}

	private void validateValue(Object value, String type, Collection<String> validations, BindingResult binding) {
		String validationName;
		String validationParams;
		Collection<String> messageParams;

		if (type.startsWith("Array") && (value instanceof Collection<?>)) {
			type = type.substring(6, type.length() - 1);
			for (Object singleValue : (Collection<?>) value) {
				this.validateValue(singleValue, type, validations, binding);
			}
		} else {
			for (String validation : validations) {
				if (validation.indexOf("#") >= 0) {
					validationName = validation.split("#")[0];
					validationParams = validation.split("#")[1];
				} else {
					validationName = validation;
					validationParams = "";
				}

				try {
					switch (validationName) {
					case "url":
						if (!Helpers.validateUrl((String) value)) {
							binding.rejectValue("value", "org.hibernate.validator.constraints.URL.message");
						}
						break;
					case "email":
						if (!Helpers.validateEmail((String) value)) {
							binding.rejectValue("value", "org.hibernate.validator.constraints.Email.message");
						}
						break;
					case "min":
						if (!Helpers.min(Double.valueOf(value.toString()),
								Double.valueOf(Helpers.typeFromString(type, validationParams).toString()))) {
							messageParams = new ArrayList<String>();
							messageParams.add(validationParams);
							binding.rejectValue("value", "common.error.min", messageParams.toArray(), "");
						}
						break;
					case "max":
						if (!Helpers.max(Double.valueOf(value.toString()),
								Double.valueOf(Helpers.typeFromString(type, validationParams).toString()))) {
							messageParams = new ArrayList<String>();
							messageParams.add(validationParams);
							binding.rejectValue("value", "common.error.max", messageParams.toArray(), "");
						}
						break;
					case "range":
						if (!Helpers.range(Double.valueOf(value.toString()),
								Double.valueOf(Helpers.typeFromString(type, validationParams.split(",")[0]).toString()),
								Double.valueOf(
										Helpers.typeFromString(type, validationParams.split(",")[1]).toString()))) {
							messageParams = new ArrayList<String>();
							messageParams.add(validationParams.split(",")[0]);
							messageParams.add(validationParams.split(",")[1]);
							binding.rejectValue("value", "common.error.range", messageParams.toArray(), "");
						}
						break;
					case "before":
						if (!Helpers.before((Date) value,
								(Date) Helpers.typeFromString(type, validationParams.split(",")[0]))) {
							messageParams = new ArrayList<String>();
							messageParams.add(validationParams);
							binding.rejectValue("value", "common.error.before", messageParams.toArray(), "");
						}
						break;
					case "after":
						if (!Helpers.after((Date) value,
								(Date) Helpers.typeFromString(type, validationParams.split(",")[0]))) {
							messageParams = new ArrayList<String>();
							messageParams.add(validationParams);
							binding.rejectValue("value", "common.error.after", messageParams.toArray(), "");
						}
						break;
					case "between":
						if (!Helpers.between((Date) value,
								(Date) Helpers.typeFromString(type, validationParams.split(",")[0]),
								(Date) Helpers.typeFromString(type, validationParams.split(",")[1]))) {
							messageParams = new ArrayList<String>();
							messageParams.add(validationParams.split(",")[0]);
							messageParams.add(validationParams.split(",")[1]);
							binding.rejectValue("value", "common.error.between", messageParams.toArray(), "");
						}
						break;
					}

				} catch (Exception e) {
					binding.rejectValue("value", "common.message.error");
				}
			}
		}
	}
}
