package controllers;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import services.ActorService;
import services.MessageFolderService;
import services.MessageService;
import domain.Actor;
import domain.Message;
import domain.MessageFolder;

@Controller
@RequestMapping("/message/actor")
public class MessageController extends AbstractController{
	// Services --------------------------------------------
	
		@Autowired
		private MessageService messageService;
		
		@Autowired
		private MessageFolderService messageFolderService;
		
		@Autowired
		private ActorService actorService;
		
		// Constructor -----------------------------------------
		
		public MessageController(){
			super();
		}
		
		// Listing ---------------------------------------------
		
		@RequestMapping(value="/list", method=RequestMethod.GET)
		public ModelAndView list(@RequestParam int messageFolderId){
			ModelAndView view;
			MessageFolder folder;
			Collection<Message> messages;
			
			folder = messageFolderService.findOne(messageFolderId);
			view = new ModelAndView("message/list");
			messages = messageService.findAllByFolderId(messageFolderId);
			view.addObject("messages", messages);
			view.addObject("folder", folder);
			view.addObject("requestURI", "message/actor/list.do?messageFolderId=" + messageFolderId);
			
			return view;
		}
		
		// Creation --------------------------------------------
			
		@RequestMapping(value="/create", method=RequestMethod.GET)
		public ModelAndView create(){
			ModelAndView view;
			Message message;
			
			message = messageService.create();
			view = createEditModelAndView(message);
			
			return view;
		}

		// Edition ---------------------------------------------
		
		@RequestMapping(value="/edit", method=RequestMethod.GET)
		public ModelAndView edit(@RequestParam int messageId){
			ModelAndView view;
			Message message;
			
			message = messageService.findOne(messageId);
			view = createEditModelAndView(message);
			
			return view;
		}
		
		@RequestMapping(value="/edit", method=RequestMethod.POST, params="send")
		public ModelAndView send(@Valid Message message, BindingResult binding, RedirectAttributes redirectAttrs){
			ModelAndView view;
			
			if (binding.hasErrors()) {
				System.out.println(binding.getAllErrors());
				view = createEditModelAndView(message);
			} else {
				try {
					messageService.send(message);
					view = new ModelAndView("redirect:list.do?messageFolderId=" + message.getMessageFolder().getId());
					redirectAttrs.addFlashAttribute("message", "message.commit.ok");
				} catch(Throwable oops) {
					view = createEditModelAndView(message, "message.commit.error");
				}
			}
			
			return view;
		}
		
		@RequestMapping(value="/edit", method=RequestMethod.POST, params="move")
		public ModelAndView move(@Valid Message message, BindingResult binding, RedirectAttributes redirectAttrs){
			ModelAndView view;
			
			if (binding.hasErrors()) {
				view = createEditModelAndView(message);
			} else {
				try {
					messageService.save(message);
					view = new ModelAndView("redirect:list.do?messageFolderId=" + message.getMessageFolder().getId());
					redirectAttrs.addFlashAttribute("message", "message.commit.ok");
				} catch(Throwable oops) {
					view = createEditModelAndView(message, "message.commit.error");
				}
			}
			
			return view;
		}
		
		@RequestMapping(value="/edit", method=RequestMethod.POST, params="delete")
		public ModelAndView delete(@Valid Message message, BindingResult binding, RedirectAttributes redirectAttrs){
			ModelAndView view;
			int folderId;
			
			
			try {
				folderId = message.getMessageFolder().getId();
				messageService.deleteMessage(message.getId());
				view =  new ModelAndView("redirect:list.do?messageFolderId=" + folderId);
				redirectAttrs.addFlashAttribute("message", "message.commit.ok");
			} catch (Throwable oops) {
				view = createEditModelAndView(message, "message.commit.error");
			}
			
			return view;
		}
		
		// Ancillary methods -----------------------------------
		
		protected ModelAndView createEditModelAndView(Message message){
			ModelAndView view;
			
			view = createEditModelAndView(message, null);
			
			return view;
		}
		
		protected ModelAndView createEditModelAndView(Message message, String strMessage){
			ModelAndView view;
			Collection<Actor> actors;
			Collection<MessageFolder> folders;
			
			actors = actorService.findAll();
			folders = messageFolderService.findAllMessageFolderByActorLogged();
			view = new ModelAndView("message/edit");
			view.addObject("msg", message);
			view.addObject("recipients", actors);
			view.addObject("folders", folders);
			view.addObject("message", strMessage);
			view.addObject("requestURI","message/actor/edit.do");
			view.addObject("cancelURI", "message/actor/list.do?messageFolderId=" + message.getMessageFolder().getId());
			
			return view;
		}
}
