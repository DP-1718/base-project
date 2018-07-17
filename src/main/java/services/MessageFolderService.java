
package services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Actor;
import domain.Message;
import domain.MessageFolder;
import repositories.MessageFolderRepository;
import security.UserAccount;

@Transactional
@Service
public class MessageFolderService {

	// Constructor --------------------------------------------------
	public MessageFolderService() {
		super();
	}

	// Managed Repository -------------------------------------------
	@Autowired
	private MessageFolderRepository messageFolderRepository;

	// Supported Services -------------------------------------------
	@Autowired
	private ActorService actorService;

	// CRUD methods -------------------------------------------------

	public MessageFolder create() {
		MessageFolder result;
		Collection<Message> messages;
		Actor actor;

		actor = this.actorService.getActorLogged();
		messages = new ArrayList<Message>();
		result = new MessageFolder();

		result.setMessages(messages);
		result.setActor(actor);
		result.setModificable(true);

		Assert.notNull(result);

		return result;
	}

	public MessageFolder save(final MessageFolder messageFolder) {
		MessageFolder result;

		Assert.notNull(messageFolder);

		if (messageFolder.getModificable()) {
			Assert.isTrue(!messageFolder.getName().equals("Inbox"));
			Assert.isTrue(!messageFolder.getName().equals("Outbox"));
			Assert.isTrue(!messageFolder.getName().equals("Trashbox"));
			Assert.isTrue(!messageFolder.getName().equals("Spambox"));
		}

		result = this.messageFolderRepository.save(messageFolder);
		Assert.notNull(result);

		return result;
	}

	public void delete(final MessageFolder messageFolder) {
		this.messageFolderRepository.delete(messageFolder);
	}

	public MessageFolder findOne(final int messageFolderId) {
		MessageFolder result;
		result = this.messageFolderRepository.findOne(messageFolderId);
		Assert.notNull(result);
		return result;
	}

	public Collection<MessageFolder> findAll() {
		Collection<MessageFolder> result;
		result = this.messageFolderRepository.findAll();
		return result;
	}

	// Other Business Methods ---------------------------------------

	public Collection<MessageFolder> findAllByUser() {
		Collection<MessageFolder> result;
		Actor actor;

		actor = this.actorService.getActorLogged();
		result = this.messageFolderRepository.findAllByUserId(actor.getId());

		return result;
	}

	/*
	 * Test methods: - UserServiceTest.testPositiveCreateUser
	 */
	public Collection<MessageFolder> createMessageFoldersDefault(final Actor actor) {
		Collection<MessageFolder> result;
		MessageFolder inbox, outbox, trashbox, spambox;
		Collection<Message> messages1, messages2, messages3, messages4;

		result = new ArrayList<MessageFolder>();
		inbox = new MessageFolder();
		outbox = new MessageFolder();
		trashbox = new MessageFolder();
		spambox = new MessageFolder();
		messages1 = new ArrayList<Message>();
		messages2 = new ArrayList<Message>();
		messages3 = new ArrayList<Message>();
		messages4 = new ArrayList<Message>();

		inbox.setName("Inbox");
		inbox.setActor(actor);
		inbox.setModificable(false);
		inbox.setMessages(messages1);

		outbox.setName("Outbox");
		outbox.setActor(actor);
		outbox.setModificable(false);
		outbox.setMessages(messages2);

		trashbox.setName("Trashbox");
		trashbox.setActor(actor);
		trashbox.setModificable(false);
		trashbox.setMessages(messages3);

		spambox.setName("Spambox");
		spambox.setActor(actor);
		spambox.setModificable(false);
		spambox.setMessages(messages4);

		result.add(inbox);
		result.add(outbox);
		result.add(trashbox);
		result.add(spambox);

		Assert.isTrue(result.size() == 4);

		return result;
	}

	/*
	 * Test methods: - UserServiceTest.testPositiveCreateUser
	 */
	public Collection<MessageFolder> saveMessageFoldersDefault(final Collection<MessageFolder> messageFolders,
			final Actor actor) {
		Collection<MessageFolder> result;
		MessageFolder messageFolder;

		result = new ArrayList<MessageFolder>();

		for (final MessageFolder f : messageFolders) {
			f.setActor(actor);
			messageFolder = this.messageFolderRepository.save(f);

			result.add(messageFolder);
		}

		return result;
	}

	/*
	 * Test methods: - MessageServiceTest.testPositiveExchangeMessages -
	 * MessageServiceTest.testPositiveMoveMessages
	 */
	public MessageFolder findOneByUserAccountAndName(final UserAccount userAccount, final String nameFolder) {
		MessageFolder result;

		result = this.messageFolderRepository.findOneByUserAccountAndName(userAccount.getId(), nameFolder);

		return result;
	}

	public Collection<MessageFolder> findAllMessageFolderByActorLogged() {
		Collection<MessageFolder> result;
		Actor actor;

		actor = this.actorService.getActorLogged();
		result = this.messageFolderRepository.findAllByUserId(actor.getId());

		return result;
	}

}
