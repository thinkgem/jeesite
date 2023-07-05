/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.file.web.rest;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * UEditor Controller
 * @author ThinkGem
 * @version 2023-7-5
 */
@Controller
@RequestMapping(value = "${adminPath}/file/ueditor")
@ConditionalOnProperty(name="file.enabled", havingValue="true", matchIfMissing=true)
public class UeditorController extends com.jeesite.modules.file.web.UeditorController {

}