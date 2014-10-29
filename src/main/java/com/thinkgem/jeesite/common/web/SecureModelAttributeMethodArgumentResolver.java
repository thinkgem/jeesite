package com.thinkgem.jeesite.common.web;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.Map;

import javax.servlet.ServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.core.Conventions;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.ExtendedServletRequestDataBinder;

import com.thinkgem.jeesite.common.utils.Reflections;

/**
 * @author 薛定谔的猫
 *
 */
public class SecureModelAttributeMethodArgumentResolver implements
		HandlerMethodArgumentResolver {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(SecureModelAttributeMethodArgumentResolver.class);

	private final boolean annotationNotRequired;

	public SecureModelAttributeMethodArgumentResolver(boolean annotationNotRequired) {
		this.annotationNotRequired = annotationNotRequired;
	}

	@Override
	public final boolean supportsParameter(MethodParameter parameter) {
		if (parameter.hasParameterAnnotation(SecureModelAttribute.class)) {
			return true;
		} else if (this.annotationNotRequired) {
			return !BeanUtils.isSimpleProperty(parameter.getParameterType());
		} else {
			return false;
		}
	}

	@Override
	public final Object resolveArgument(MethodParameter parameter,
			ModelAndViewContainer mavContainer, NativeWebRequest request,
			WebDataBinderFactory binderFactory) throws Exception {

		// 从注解中获取model名,如注解中为空白或无注解则使用参数名
		SecureModelAttribute annot = parameter.getParameterAnnotation(SecureModelAttribute.class);
		String attrName = (annot != null) ? annot.value() : null;
		String name = StringUtils.hasText(attrName) ? attrName : Conventions.getVariableNameForParameter(parameter);

		// 获取绑定对象,如果model中存在直接获取否则新建一个
		Object attribute = (mavContainer.containsAttribute(name)) ? 
				mavContainer.getModel().get(name) : createAttribute(name, parameter, binderFactory, request);

		String[] clearFields = annot.clearFiled();
		for (String clearField : clearFields) {
			if (StringUtils.hasText(clearField)) {
				Reflections.invokeMethod(Reflections.getFieldValue(attribute, clearField), "clear", null, null);
			}
		}

		// 从绑定工厂获取绑定类
		WebDataBinder binder = (ExtendedServletRequestDataBinder) binderFactory.createBinder(request, attribute, name);
		if (binder.getTarget() != null) {

			// 设置绑定允许的字段和禁止的字段
			if (annot != null) {
				String[] allowedFields = annot.allowedField();
				String[] deniedFields = annot.deniedField();
				binder.setAllowedFields(allowedFields);
				binder.setDisallowedFields(deniedFields);
			}

			// 绑定request参数
			bindRequestParameters(binder, request);
			
			// 如果需要进行认证,见 @Valid注解
			validateIfApplicable(binder, parameter);
			if (binder.getBindingResult().hasErrors()) {
				
				// 如果不紧跟Errors参数直接抛出异常
				if (isBindExceptionRequired(binder, parameter)) {
					throw new BindException(binder.getBindingResult());
				}
			}
		}

		// 将绑定结果和认证结果添加到model
		Map<String, Object> bindingResultModel = binder.getBindingResult()
				.getModel();
		mavContainer.removeAttributes(bindingResultModel);
		mavContainer.addAllAttributes(bindingResultModel);

		return binder.getTarget();
	}

	private Object createAttribute(String attributeName,
			MethodParameter parameter, WebDataBinderFactory binderFactory,
			NativeWebRequest request) throws Exception {

		// 如果request中存在该attributeName,则尝试用ConversionService转换
		String value = getRequestValueForAttribute(attributeName, request);
		if (value != null) {
			Object attribute = createAttributeFromRequestValue(value, attributeName, parameter, binderFactory, request);
			if (attribute != null) {
				return attribute;
			}
		}

		// 否则直接创建
		return BeanUtils.instantiateClass(parameter.getParameterType());
	}

	// 从request中获取attributeName
	private String getRequestValueForAttribute(String attributeName, NativeWebRequest request) {
		Map<String, String> variables = getUriTemplateVariables(request);
		if (StringUtils.hasText(variables.get(attributeName))) {
			return variables.get(attributeName);
		} else if (StringUtils.hasText(request.getParameter(attributeName))) {
			return request.getParameter(attributeName);
		} else {
			return null;
		}
	}

	// 获取UriTemplateVariables
	@SuppressWarnings("unchecked")
	private Map<String, String> getUriTemplateVariables(NativeWebRequest request) {
		Map<String, String> variables = (Map<String, String>) request
				.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, 
						RequestAttributes.SCOPE_REQUEST);
		return (variables != null) ? variables : Collections.<String, String> emptyMap();
	}

	// 从requestValue创建Attribute
	private Object createAttributeFromRequestValue(String sourceValue,
			String attributeName, MethodParameter parameter,
			WebDataBinderFactory binderFactory, NativeWebRequest request)
			throws Exception {
		
		DataBinder binder = binderFactory.createBinder(request, null, attributeName);
		ConversionService conversionService = binder.getConversionService();
		if (conversionService != null) {
			TypeDescriptor source = TypeDescriptor.valueOf(String.class);
			TypeDescriptor target = new TypeDescriptor(parameter);
			if (conversionService.canConvert(source, target)) {
				return binder.convertIfNecessary(sourceValue, parameter.getParameterType(), parameter);
			}
		}
		return null;
	}

	// 绑定参数
	private void bindRequestParameters(WebDataBinder binder, NativeWebRequest request) {
		ServletRequest servletRequest = request.getNativeRequest(ServletRequest.class);
		ServletRequestDataBinder servletBinder = (ServletRequestDataBinder) binder;
		servletBinder.bind(servletRequest);
	}

	// 判断是否需要jsr303认证
	private void validateIfApplicable(WebDataBinder binder, MethodParameter parameter) {
		Annotation[] annotations = parameter.getParameterAnnotations();
		for (Annotation annot : annotations) {
			if (annot.annotationType().getSimpleName().startsWith("Valid")) {
				Object hints = AnnotationUtils.getValue(annot);
				binder.validate(hints instanceof Object[] ? (Object[]) hints : new Object[] { hints });
				break;
			}
		}
	}

	// 判断是否需要抛出绑定异常
	private boolean isBindExceptionRequired(WebDataBinder binder, MethodParameter parameter) {
		int i = parameter.getParameterIndex();
		Class<?>[] paramTypes = parameter.getMethod().getParameterTypes();
		boolean hasBindingResult = (paramTypes.length > (i + 1) && Errors.class.isAssignableFrom(paramTypes[i + 1]));
		return !hasBindingResult;
	}

}
