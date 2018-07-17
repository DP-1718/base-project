/*
 * AbstractController.java
 *
 * Copyright (C) 2017 Universidad de Sevilla
 *
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import services.AdministratorService;
import services.ConfigurationService;

@Controller
public class AbstractController {

	@Autowired
	protected ConfigurationService configurationService;

	@Autowired
	protected AdministratorService administratorService;

	// Default view
	protected ModelAndView createView(final String viewName) {
		return this.createView(viewName, false);
	}

	protected ModelAndView createView(final String viewName, final boolean isRedirection) {
		final ModelAndView view = new ModelAndView(viewName);

		if (!isRedirection) {
			view.addObject("companyName", this.configurationService.getEntryByName("companyName"));
		}

		return view;
	}

	// Panic handler ----------------------------------------------------------

	@ExceptionHandler(Throwable.class)
	public ModelAndView panic(final Throwable oops) {
		ModelAndView result;

		result = new ModelAndView("misc/panic");
		result.addObject("name", ClassUtils.getShortName(oops.getClass()));
		result.addObject("exception", oops.getMessage());
		result.addObject("stackTrace", ExceptionUtils.getStackTrace(oops));

		return result;
	}

}
