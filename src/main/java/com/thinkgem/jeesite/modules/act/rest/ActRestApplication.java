/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.act.rest;

import org.activiti.rest.common.api.DefaultResource;
import org.activiti.rest.common.application.ActivitiRestApplication;
import org.activiti.rest.common.filter.JsonpFilter;
import org.activiti.rest.diagram.application.DiagramServicesInit;
import org.activiti.rest.editor.application.ModelerServicesInit;
import org.restlet.Restlet;
import org.restlet.routing.Router;

/**
 * Activit Rest
 * @author ThinkGem
 * @version 2013-11-03
 */
public class ActRestApplication extends ActivitiRestApplication {

	public ActRestApplication() {
		super();
	}

	/**
	 * Creates a root Restlet that will receive all incoming calls.
	 */
	@Override
	public synchronized Restlet createInboundRoot() {
		Router router = new Router(getContext());
		router.attachDefault(DefaultResource.class);
		ModelerServicesInit.attachResources(router);
		DiagramServicesInit.attachResources(router);
		JsonpFilter jsonpFilter = new JsonpFilter(getContext());
		jsonpFilter.setNext(router);
		return jsonpFilter;
	}

}
