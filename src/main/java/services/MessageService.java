
package services;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Actor;
import domain.Administrator;
import domain.Message;
import domain.MessageFolder;
import domain.SpamKeyword;
import repositories.MessageRepository;
import security.UserAccount;

@Service
@Transactional
public class MessageService {

	// Constructors --------------------------------------------------

	public MessageService() {
		super();
	}

	// Managed respository --------------------------------------------

	@Autowired
	private MessageRepository messageRepository;

	// Supporting Services -------------------------------------------

	@Autowired
	private ActorService actorService;

	@Autowired
	private MessageFolderService messageFolderService;

	@Autowired
	private SpamKeywordService spamKeywordService;

	@Autowired
	private AdministratorService administratorService;

	// CRUD methods -------------------------------------------------

	/*
	 * Test methods: - MessageServiceTest.testPositiveExchangeMessages -
	 * MessageServiceTest.testPositiveMoveMessages
	 */
	public Message create() {
		Message result;
		MessageFolder folder;
		Actor principal;
		Date now;

		result = new Message();
		principal = this.actorService.getActorLogged();
		folder = this.messageFolderService.findOneByUserAccountAndName(principal.getUserAccount(), "Outbox");
		now = new Date(System.currentTimeMillis() - 10);

		Assert.notNull(principal);

		result.setSender(principal);
		result.setMoment(now);
		result.setMessageFolder(folder);

		return result;
	}

	/*
	 * Test methods: - MessageServiceTest.testPositiveExchangeMessages -
	 * MessageServiceTest.testPositiveMoveMessages
	 */
	public Message create(final Message message) {
		Message result;
		Actor recipient;
		Actor sender;

		result = new Message();
		recipient = message.getRecipient();
		sender = message.getSender();

		Assert.isTrue(this.actorService.getActorLogged().equals(sender));

		result.setSender(sender);
		result.setRecipient(recipient);
		result.setBody(message.getBody());
		result.setSubject(message.getSubject());
		result.setMoment(message.getMoment());
		result.setPriority(message.getPriority());

		return result;
	}

	/*
	 * Test methods: - MessageServiceTest.testNegativeDeleteMessages
	 */
	public void deleteMessage(final int messageId) {
		UserAccount folderOwner;
		Message message;

		message = this.findOne(messageId);
		folderOwner = message.getMessageFolder().getActor().getUserAccount();

		Assert.isTrue(this.actorService.getActorLogged().equals(folderOwner));

		if (message.getMessageFolder().getName().equals("Trashbox")) {
			this.messageRepository.delete(message);
		} else {
			MessageFolder trashBox;

			trashBox = this.messageFolderService.findOneByUserAccountAndName(folderOwner, "Trashbox");

			message.setMessageFolder(trashBox);

			this.save(message);
		}
	}

	public Message findOne(final int messageId) {
		Message result;
		result = this.messageRepository.findOne(messageId);
		Assert.notNull(result);
		return result;

	}

	public Collection<Message> findAll() {
		Collection<Message> result;

		result = this.messageRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	/*
	 * Test methods: - MessageServiceTest.testPositiveMoveMessages
	 */
	public Message save(final Message message) {
		Message result;
		UserAccount folderOwner;

		folderOwner = message.getMessageFolder().getActor().getUserAccount();

		Assert.isTrue(this.actorService.getActorLogged().equals(folderOwner));

		result = this.messageRepository.save(message);
		Assert.notNull(result);
		return result;

	}

	// Other Business Methods ---------------------------------------

	/*
	 * Test methods: - MessageServiceTest.testPositiveExchangeMessages -
	 * MessageServiceTest.testPositiveMoveMessages
	 */
	public Collection<Message> findAllByFolderId(final Integer messageFolderId) {
		Collection<Message> messages;
		MessageFolder messageFolder;

		messageFolder = this.messageFolderService.findOne(messageFolderId);
		Assert.isTrue(this.actorService.getActorLogged().equals(messageFolder.getActor().getUserAccount()));

		messages = this.messageRepository.findAllByFolder(messageFolder);

		return messages;
	}

	/*
	 * Test methods: - MessageServiceTest.testPositiveExchangeMessages -
	 * MessageServiceTest.testPositiveMoveMessages
	 */
	public Message send(final Message message) {
		Message result;
		Message recipientMessage;
		Date now;
		Collection<SpamKeyword> keywords;
		MessageFolder folder;

		Assert.isTrue(this.actorService.getActorLogged().equals(message.getSender().getUserAccount()));
		Assert.isTrue(
				this.actorService.getActorLogged().equals(message.getMessageFolder().getActor().getUserAccount()));
		keywords = this.spamKeywordService.findSpamKeywords(message);

		if (keywords.isEmpty()) {
			folder = this.messageFolderService.findOneByUserAccountAndName(message.getRecipient().getUserAccount(),
					"Inbox");
		} else {
			folder = this.messageFolderService.findOneByUserAccountAndName(message.getRecipient().getUserAccount(),
					"Spambox");
		}

		result = message;
		now = new Date(System.currentTimeMillis() - 10);

		result.setMoment(now);

		result = this.messageRepository.save(result);
		recipientMessage = this.create(result);
		recipientMessage.setMessageFolder(folder);
		recipientMessage = this.messageRepository.save(recipientMessage);

		return result;
	}

	public Message createMessageBulk() {
		Message result;
		Administrator administrator;
		Date now;

		administrator = this.administratorService.getAdministratorLogged();

		result = new Message();
		now = new Date(System.currentTimeMillis() - 10);

		result.setSender(administrator);
		result.setMoment(now);
		result.setPriority(0);

		return result;
	}

	public Message sendMessageBulk(final Message message) {
		Message result;
		Collection<SpamKeyword> keyWordsSpam;
		MessageFolder folder;
		Date now;

		keyWordsSpam = this.spamKeywordService.findSpamKeywords(message);
		now = new Date(System.currentTimeMillis() - 1);

		if (keyWordsSpam.isEmpty()) {
			folder = this.messageFolderService.findOneByUserAccountAndName(message.getRecipient().getUserAccount(),
					"Inbox");
		} else {
			folder = this.messageFolderService.findOneByUserAccountAndName(message.getRecipient().getUserAccount(),
					"Spambox");
		}

		message.setMoment(now);
		message.setMessageFolder(folder);

		result = this.messageRepository.save(message);

		return result;
	}

}
