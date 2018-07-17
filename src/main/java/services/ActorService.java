package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import domain.Actor;
import repositories.ActorRepository;
import security.LoginService;
import security.UserAccount;

@Service
@Transactional
public class ActorService {

	@Autowired
	private ActorRepository actorRepository;

	public Collection<Actor> findAll() {
		Collection<Actor> actors;

		actors = this.actorRepository.findAll();
		Assert.notNull(actors);

		return actors;
	}

	/*
	 * Check if actor is logged
	 */
	public Boolean isActorLogged() {
		Boolean result;

		result = true;
		try {
			Assert.notNull(this.getActorLogged());
		} catch (final Exception e) {
			result = false;
		}

		return result;
	}

	/*
	 * Get actor logged. Throw exception if actor is not logged.
	 */
	public Actor getActorLogged() {
		UserAccount userAccount;
		Actor result;

		userAccount = LoginService.getPrincipal();
		result = this.actorRepository.findOneByPrincipal(userAccount.getId());

		Assert.notNull(result);

		return result;
	}
}