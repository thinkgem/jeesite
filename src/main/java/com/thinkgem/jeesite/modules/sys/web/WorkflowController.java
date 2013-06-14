package com.thinkgem.jeesite.modules.sys.web;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.workflow.WorkflowProcessDefinitionService;
import com.thinkgem.jeesite.modules.sys.service.workflow.WorkflowTraceService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import com.thinkgem.jeesite.modules.sys.utils.workflow.Variable;

@Controller
@RequestMapping(value = "${adminPath}/sys/workflow")
public class WorkflowController {


	  protected Logger logger = LoggerFactory.getLogger(getClass());

	  protected WorkflowProcessDefinitionService workflowProcessDefinitionService;

	  protected RepositoryService repositoryService;

	  protected RuntimeService runtimeService;

	  protected TaskService taskService;

	  protected WorkflowTraceService traceService;

	  protected static Map<String, ProcessDefinition> PROCESS_DEFINITION_CACHE = new HashMap<String, ProcessDefinition>();

	  /**
	   * 流程定义列表
	   * 
	   * @return
	   */
	  @RequestMapping(value = "/processList")
	  public ModelAndView processList() {
	    ModelAndView mav = new ModelAndView("modules/sys/processList");

	    /*
	     * 保存两个对象，一个是ProcessDefinition（流程定义），一个是Deployment（流程部署）
	     */
	    List<Object[]> objects = new ArrayList<Object[]>();

	    List<ProcessDefinition> processDefinitionList = repositoryService.createProcessDefinitionQuery().list();
	    for (ProcessDefinition processDefinition : processDefinitionList) {
	      String deploymentId = processDefinition.getDeploymentId();
	      Deployment deployment = repositoryService.createDeploymentQuery().deploymentId(deploymentId).singleResult();
	      objects.add(new Object[] { processDefinition, deployment });
	    }

	    mav.addObject("objects", objects);

	    return mav;
	  }

	  /**
	   * 部署全部流程
	   * 
	   * @return
	   * @throws Exception
	   */
	  @RequestMapping(value = "/redeploy/all")
	  public String redeployAll() throws Exception {
	    workflowProcessDefinitionService.deployAllFromClasspath();
	    return "redirect:"+Global.getAdminPath()+"/sys/workflow/processList";
	  }

	  /**
	   * 读取资源，通过部署ID
	   * 
	   * @param deploymentId
	   *          流程部署的ID
	   * @param resourceName
	   *          资源名称(foo.xml|foo.png)
	   * @param response
	   * @throws Exception
	   */
	  @RequestMapping(value = "/resource/deployment")
	  public void loadByDeployment(@RequestParam("deploymentId") String deploymentId, @RequestParam("resourceName") String resourceName,
	          HttpServletResponse response) throws Exception {
	    InputStream resourceAsStream = repositoryService.getResourceAsStream(deploymentId, resourceName);
	    byte[] b = new byte[1024];
	    int len = -1;
	    while ((len = resourceAsStream.read(b, 0, 1024)) != -1) {
	      response.getOutputStream().write(b, 0, len);
	    }
	  }

	  /**
	   * 读取资源，通过流程ID
	   * 
	   * @param resourceType
	   *          资源类型(xml|image)
	   * @param processInstanceId
	   *          流程实例ID
	   * @param response
	   * @throws Exception
	   */
	  @RequestMapping(value = "/resource/process-instance")
	  public void loadByProcessInstance(@RequestParam("type") String resourceType, @RequestParam("pid") String processInstanceId, HttpServletResponse response)
	          throws Exception {
	    InputStream resourceAsStream = null;
	    ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
	    ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processInstance.getProcessDefinitionId())
	            .singleResult();

	    String resourceName = "";
	    if (resourceType.equals("image")) {
	      resourceName = processDefinition.getDiagramResourceName();
	    } else if (resourceType.equals("xml")) {
	      resourceName = processDefinition.getResourceName();
	    }
	    resourceAsStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), resourceName);
	    byte[] b = new byte[1024];
	    int len = -1;
	    while ((len = resourceAsStream.read(b, 0, 1024)) != -1) {
	      response.getOutputStream().write(b, 0, len);
	    }
	  }

	  /**
	   * 删除部署的流程，级联删除流程实例
	   * 
	   * @param deploymentId
	   *          流程部署ID
	   */
	  @RequestMapping(value = "/process/delete")
	  public String delete(@RequestParam("deploymentId") String deploymentId) {
	    repositoryService.deleteDeployment(deploymentId, true);
	    return "redirect:"+Global.getAdminPath()+"/sys/workflow/processList";
	  }

	  /**
	   * 输出跟踪流程信息
	   * 
	   * @param processInstanceId
	   * @return
	   * @throws Exception
	   */
	  @RequestMapping(value = "/process/trace")
	  @ResponseBody
	  public List<Map<String, Object>> traceProcess(@RequestParam("pid") String processInstanceId) throws Exception {
	    List<Map<String, Object>> activityInfos = traceService.traceProcess(processInstanceId);
	    return activityInfos;
	  }

	  @RequestMapping(value = "/deploy")
	  public String deploy(@RequestParam(value = "file", required = false) MultipartFile file) {

	    String fileName = file.getOriginalFilename();

	    try {
	      InputStream fileInputStream = file.getInputStream();

	      String extension = FilenameUtils.getExtension(fileName);
	      if (extension.equals("zip") || extension.equals("bar")) {
	        ZipInputStream zip = new ZipInputStream(fileInputStream);
	        repositoryService.createDeployment().addZipInputStream(zip).deploy();
	      } else if (extension.equals("png")) {
	        repositoryService.createDeployment().addInputStream(fileName, fileInputStream).deploy();
	      } else if (fileName.indexOf("bpmn20.xml") != -1) {
	        repositoryService.createDeployment().addInputStream(fileName, fileInputStream).deploy();
	      } else if (extension.equals("bpmn")) {
	        /*
	         * bpmn扩展名特殊处理，转换为bpmn20.xml
	         */
	        String baseName = FilenameUtils.getBaseName(fileName);
	        repositoryService.createDeployment().addInputStream(baseName + ".bpmn20.xml", fileInputStream).deploy();
	      } else {
	        throw new ActivitiException("no support file type of " + extension);
	      }
	    } catch (Exception e) {
	      logger.error("error on deploy process, because of file input stream", e);
	    }

	    return "redirect:"+Global.getAdminPath()+"/sys/workflow/processList";
	  }

	  /**
	   * 待办任务--Portlet
	   */
	  @RequestMapping(value = "/task/todo/list")
	  @ResponseBody
	  public List<Map<String, Object>> todoList(HttpSession session) throws Exception {
	    User user = UserUtils.getUser();
	    String userId = ObjectUtils.toString(user.getId());
	    List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");

	    // 已经签收的任务
	    List<Task> todoList = taskService.createTaskQuery().taskAssignee(userId).active().list();
	    for (Task task : todoList) {
	      String processDefinitionId = task.getProcessDefinitionId();
	      ProcessDefinition processDefinition = getProcessDefinition(processDefinitionId);

	      Map<String, Object> singleTask = packageTaskInfo(sdf, task, processDefinition);
	      singleTask.put("status", "todo");
	      result.add(singleTask);
	    }

	    // 等待签收的任务
	    List<Task> toClaimList = taskService.createTaskQuery().taskCandidateUser(userId).active().list();
	    for (Task task : toClaimList) {
	      String processDefinitionId = task.getProcessDefinitionId();
	      ProcessDefinition processDefinition = getProcessDefinition(processDefinitionId);

	      Map<String, Object> singleTask = packageTaskInfo(sdf, task, processDefinition);
	      singleTask.put("status", "claim");
	      result.add(singleTask);
	    }

	    return result;
	  }

	  private Map<String, Object> packageTaskInfo(SimpleDateFormat sdf, Task task, ProcessDefinition processDefinition) {
	    Map<String, Object> singleTask = new HashMap<String, Object>();
	    singleTask.put("id", task.getId());
	    singleTask.put("name", task.getName());
	    singleTask.put("createTime", sdf.format(task.getCreateTime()));
	    singleTask.put("pdname", processDefinition.getName());
	    singleTask.put("pdversion", processDefinition.getVersion());
	    singleTask.put("pid", task.getProcessInstanceId());
	    return singleTask;
	  }

	  private ProcessDefinition getProcessDefinition(String processDefinitionId) {
	    ProcessDefinition processDefinition = PROCESS_DEFINITION_CACHE.get(processDefinitionId);
	    if (processDefinition == null) {
	      processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
	      PROCESS_DEFINITION_CACHE.put(processDefinitionId, processDefinition);
	    }
	    return processDefinition;
	  }

	  /**
	   * 挂起、激活流程实例
	   */
	  @RequestMapping(value = "processdefinition/update/{state}/{processDefinitionId}")
	  public String updateState(@PathVariable("state") String state, @PathVariable("processDefinitionId") String processDefinitionId,
	          RedirectAttributes redirectAttributes) {
	    if (state.equals("active")) {
	      redirectAttributes.addFlashAttribute("message", "已激活ID为[" + processDefinitionId + "]的流程定义。");
	      repositoryService.activateProcessDefinitionById(processDefinitionId, true, null);
	    } else if (state.equals("suspend")) {
	      repositoryService.suspendProcessDefinitionById(processDefinitionId, true, null);
	      redirectAttributes.addFlashAttribute("message", "已挂起ID为[" + processDefinitionId + "]的流程定义。");
	    }
	    return "redirect:"+Global.getAdminPath()+"/sys/workflow/processList";
	  }
	  
		/**
		 * 完成任务
		 * @param id
		 * @return
		 */
		@RequestMapping(value = "complete/{id}", method = { RequestMethod.POST, RequestMethod.GET })
		@ResponseBody
		public String complete(@PathVariable("id") String taskId, Variable var) {
			try {
				Map<String, Object> variables = var.getVariableMap();
				taskService.complete(taskId, variables);
				return "success";
			} catch (Exception e) {
				logger.error("error on complete task {}, variables={}", new Object[] { taskId, var.getVariableMap(), e });
				return "error";
			}
		}

		/**
		 * 签收任务
		 */
		@RequestMapping(value = "claim/{id}")
		@ResponseBody
		public String claim(@PathVariable("id") String taskId, HttpSession session, RedirectAttributes redirectAttributes) {
			String userId = ObjectUtils.toString(UserUtils.getUser().getId());
			taskService.claim(taskId, userId);
			return "success";
		}

		

	  @Autowired
	  public void setWorkflowProcessDefinitionService(WorkflowProcessDefinitionService workflowProcessDefinitionService) {
	    this.workflowProcessDefinitionService = workflowProcessDefinitionService;
	  }

	  @Autowired
	  public void setRepositoryService(RepositoryService repositoryService) {
	    this.repositoryService = repositoryService;
	  }

	  @Autowired
	  public void setRuntimeService(RuntimeService runtimeService) {
	    this.runtimeService = runtimeService;
	  }

	  @Autowired
	  public void setTraceService(WorkflowTraceService traceService) {
	    this.traceService = traceService;
	  }

	  @Autowired
	  public void setTaskService(TaskService taskService) {
	    this.taskService = taskService;
	  }
}
