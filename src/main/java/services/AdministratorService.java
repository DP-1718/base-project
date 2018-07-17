package services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import domain.Administrator;
import repositories.AdministratorRepository;
import security.LoginService;
import security.UserAccount;

@Service
@Transactional
public class AdministratorService {

	@Autowired
	private AdministratorRepository administratorRepository;

	public AdministratorService() {
		super();
	}

	/*
	 * Check if administrator is logged
	 */
	public Boolean isAdministratorLogged() {
		Boolean result;

		result = true;
		try {
			Assert.notNull(this.getAdministratorLogged());
		} catch (final Exception e) {
			result = false;
		}

		return result;
	}

	/*
	 * Get administrator logged. Throw exception if administrator is not logged.
	 */
	public Administrator getAdministratorLogged() {
		UserAccount userAccount;
		Administrator result;

		userAccount = LoginService.getPrincipal();
		result = this.administratorRepository.findOneByPrincipal(userAccount.getId());

		Assert.notNull(result);

		return result;
	}
}