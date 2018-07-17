package controllers.administrator;

import java.util.Collection;

import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import controllers.AbstractController;
import domain.Configuration;

@Controller
@RequestMapping("/configuration/administrator")
public class ConfigurationAdministratorController extends AbstractController {

	public ConfigurationAdministratorController() {
		super();
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView getList(RedirectAttributes redirectAttrs) {
		ModelAndView view;
		Collection<Configuration> configurationEntries;

		if (this.administratorService.isAdministratorLogged()) {
			configurationEntries = super.configurationService.findAll();

			view = super.createView("configuration/list");
			view.addObject("configurationEntries", configurationEntries);
			view.addObject("requestURI", "configuration/administrator/list.do");
		} else {
			view = super.createView("redirect:/", true);
			redirectAttrs.addFlashAttribute("message", "common.error.access");
		}

		return view;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam(required = true) int configurationId, RedirectAttributes redirectAttrs) {
		ModelAndView view;
		Configuration configuration;

		if (this.administratorService.isAdministratorLogged()) {
			configuration = this.configurationService.findOneToEdit(configurationId);
			Assert.notNull(configuration);
			view = this.editView(configuration);
		} else {
			view = super.createView("redirect:/", true);
			redirectAttrs.addFlashAttribute("message", "common.error.access");
		}

		return view;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView edit(Configuration configuration, BindingResult binding, RedirectAttributes redirectAttrs) {
		ModelAndView view;

		try {
			configuration = this.configurationService.reconstruct(configuration, binding);
			if (binding.hasErrors()) {
				view = this.editView(configuration);
			} else {
				configuration = this.configurationService.save(configuration);
				view = super.createView("redirect:list.do", true);
				redirectAttrs.addFlashAttribute("message", "common.message.success");
			}
		} catch (Throwable oops) {
			if (binding.hasErrors()) {
				view = this.editView(configuration);
			} else {
				view = this.editView(configuration, "common.message.error");
			}
		}

		return view;
	}

	private ModelAndView editView(Configuration configuration) {
		return this.editView(configuration, null);
	}

	private ModelAndView editView(Configuration configuration, String message) {
		ModelAndView view;

		view = super.createView("configuration/edit");
		view.addObject("configuration", configuration);
		view.addObject("message", message);
		view.addObject("requestURI", "configuration/administrator/edit.do");

		return view;
	}
}
