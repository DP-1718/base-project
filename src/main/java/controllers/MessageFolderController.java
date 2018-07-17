
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

import services.MessageFolderService;
import domain.MessageFolder;

@Controller
@RequestMapping("/messageFolder/actor")
public class MessageFolderController extends AbstractController {

	// Constructors -----------------------------------------------------------

	public MessageFolderController() {
		super();
	}


	// Services ---------------------------------------------------------------

	@Autowired
	private MessageFolderService	messageFolderService;


	// Listing ----------------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView myMessageFolders() {
		ModelAndView result;
		String requestURI;
		Collection<MessageFolder> messageFolders;

		messageFolders = this.messageFolderService.findAllMessageFolderByActorLogged();
		requestURI = "messageFolder/actor/list.do";

		result = new ModelAndView("messageFolder/list");
		result.addObject("messageFolders", messageFolders);
		result.addObject("requestURI", requestURI);

		return result;
	}

	// Creation ---------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		MessageFolder messageFolder;

		messageFolder = this.messageFolderService.create();
		result = this.createEditModelAndView(messageFolder);
		return result;
	}

	// Edition ----------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int messageFolderId) {
		ModelAndView view;
		MessageFolder messageFolder;

		messageFolder = this.messageFolderService.findOne(messageFolderId);
		view = this.createEditModelAndView(messageFolder);

		return view;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final MessageFolder messageFolder, final BindingResult binding, final RedirectAttributes redirectAttrs) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(messageFolder);
		else
			try {
				this.messageFolderService.save(messageFolder);
				result = new ModelAndView("redirect:list.do");
				redirectAttrs.addFlashAttribute("message", "messageFolder.commit.ok");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(messageFolder, "messageFolder.commit.error");
			}

		return result;
	}

	// Delete ----------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(@Valid final MessageFolder messageFolder, final BindingResult binding, final RedirectAttributes redirectAttrs) {
		ModelAndView view;

		try {
			this.messageFolderService.delete(messageFolder);
			view = new ModelAndView("redirect:list.do");
			redirectAttrs.addFlashAttribute("message", "messageFolder.commit.ok");
		} catch (final Throwable oops) {
			view = this.createEditModelAndView(messageFolder, "messageFolder.commit.error");
		}

		return view;
	}

	// Ancillary methods ------------------------------------------------------

	protected ModelAndView createEditModelAndView(final MessageFolder messageFolder) {
		ModelAndView result;

		result = this.createEditModelAndView(messageFolder, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final MessageFolder messageFolder, final String message) {
		ModelAndView result;

		result = new ModelAndView("messageFolder/edit");

		result.addObject("messageFolder", messageFolder);
		result.addObject("message", message);
		result.addObject("requestURI", "messageFolder/actor/edit.do");

		return result;
	}

}
