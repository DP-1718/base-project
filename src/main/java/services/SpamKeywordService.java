
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import domain.Message;
import domain.SpamKeyword;
import repositories.SpamKeywordRepository;

@Service
@Transactional
public class SpamKeywordService {

	// Constructors -----------------------------

	public SpamKeywordService() {
		super();
	}

	// Managed respository ----------------------

	@Autowired
	private SpamKeywordRepository spamKeywordRepository;

	// Supporting Services ----------------------
	@Autowired
	private AdministratorService administratorService;

	@Autowired
	Validator validator;

	// CRUD methods -------------------------------------------------
	public SpamKeyword create() {
		SpamKeyword result;

		this.administratorService.getAdministratorLogged();

		result = new SpamKeyword();

		Assert.notNull(result);

		return result;
	}

	public SpamKeyword findOne(final Integer keyWordId) {
		SpamKeyword result;

		Assert.notNull(keyWordId);
		result = this.spamKeywordRepository.findOne(keyWordId);
		Assert.notNull(result);

		return result;
	}

	public Collection<SpamKeyword> findAll() {
		Collection<SpamKeyword> result;

		result = this.spamKeywordRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public SpamKeyword save(final SpamKeyword keyWord) {
		SpamKeyword result;

		Assert.notNull(keyWord);
		this.administratorService.getAdministratorLogged();

		result = this.spamKeywordRepository.save(keyWord);

		Assert.notNull(result);

		return result;
	}

	public SpamKeyword delete(final SpamKeyword keyWord) {

		Assert.notNull(keyWord);
		this.administratorService.getAdministratorLogged();

		this.spamKeywordRepository.delete(keyWord);

		return keyWord;
	}

	public SpamKeyword reconstruct(SpamKeyword keyword, BindingResult binding) {
		SpamKeyword keywordReconstructed;

		if (keyword.getId() == 0) {
			keywordReconstructed = this.create();
		} else {
			keywordReconstructed = this.findOne(keyword.getId());
		}

		keywordReconstructed.setWord(keyword.getWord());

		this.validator.validate(keywordReconstructed, binding);

		Assert.isTrue(!binding.hasErrors());

		return keywordReconstructed;
	}

	// Other Business Methods ---------------------------------------

	/*
	 * Test methods: - MessageServiceTest.testPositiveExchangeMessages -
	 * MessageServiceTest.testPositiveMoveMessages
	 */
	public Collection<SpamKeyword> findSpamKeywords(final Message message) {
		Collection<SpamKeyword> result;

		result = this.spamKeywordRepository.findSpamKeywords(message.getSubject(), message.getBody());

		return result;
	}

}
