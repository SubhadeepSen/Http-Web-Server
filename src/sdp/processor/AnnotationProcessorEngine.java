package sdp.processor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sdp.annotation.AutoInject;
import sdp.annotation.DefineComponent;
import sdp.annotation.DefineController;
import sdp.annotation.HandlerMethod;

/**
 * The AnnotationProcessorEngine processes the annotations, creates the objects
 * of the annotated classes, injects the dependencies to the fields, and invoke
 * the handler method by mapping the handler name, http method and injecting the
 * required arguments.
 * 
 * @author Subhadeep Sen
 */
@SuppressWarnings("rawtypes")
public class AnnotationProcessorEngine {
	private AnnotationProcessorConfig annotationProcessorConfig;

	public AnnotationProcessorEngine(AnnotationProcessorConfig annotationProcessorConfig) {
		this.annotationProcessorConfig = annotationProcessorConfig;
	}

	/**
	 * starts the processing engine
	 * 
	 * @return Object
	 */
	public Object process() {
		Object returnObject = null;
		String packageName = annotationProcessorConfig.getPackageName();
		AnnotatedClassLoader annotatedClassLoader = new AnnotatedClassLoader();
		try {
			ClassLoader classLoader = this.getClass().getClassLoader();
			List<Class> availableClasses = annotatedClassLoader.getClasses(classLoader, packageName);
			Map<String, Object> annotatedClassObjects = annotatedClassProcessor(availableClasses);
			if (!annotatedClassObjects.isEmpty()) {
				addExternalObjects(annotatedClassObjects);
				annotatedFieldProcessor(annotatedClassObjects);
				returnObject = annotatedMethodProcessor(annotatedClassObjects);
			} else {
				System.out.println("No annotated class has been found in : " + packageName);
			}
		} catch (Exception e) {
			System.out.println("[ProcessorEngine] Unable to process : " + e.getMessage());
		}
		return returnObject;
	}

	/**
	 * adds external objects to the container
	 * 
	 * @param annotatedClassObjects
	 */
	private void addExternalObjects(Map<String, Object> annotatedClassObjects) {
		if (null != annotationProcessorConfig.getObjects()) {
			for (Object object : annotationProcessorConfig.getObjects()) {
				annotatedClassObjects.put(object.getClass().getName(), object);
			}
		}
	}

	/**
	 * creates a container of objects of all the annotated classes
	 * 
	 * @param availableClasses
	 * @return Map
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "deprecation" })
	private Map<String, Object> annotatedClassProcessor(List<Class> availableClasses) throws Exception {
		Map<String, Object> annotatedClassObjects = new HashMap<>();
		for (Class availableClass : availableClasses) {
			if (availableClass.isAnnotationPresent(DefineController.class)
					|| availableClass.isAnnotationPresent(DefineComponent.class)) {
				annotatedClassObjects.put(availableClass.getName(),
						Class.forName(availableClass.getName()).newInstance());
			}
		}
		return annotatedClassObjects;
	}

	/**
	 * inject the dependencies to the annotated fields
	 * 
	 * @param annotatedClassObjects
	 * @throws IllegalAccessException
	 */
	private void annotatedFieldProcessor(Map<String, Object> annotatedClassObjects) throws IllegalAccessException {
		Object object = null;
		Field[] declaredFields = null;
		for (String className : annotatedClassObjects.keySet()) {
			object = annotatedClassObjects.get(className);
			declaredFields = object.getClass().getDeclaredFields();
			for (Field field : declaredFields) {
				if (field.isAnnotationPresent(AutoInject.class)) {
					field.setAccessible(true);
					field.set(object, annotatedClassObjects.get(field.getType().getName()));
					field.setAccessible(false);
				}
			}
		}
	}

	/**
	 * invokes the annotated method which matched the handler name and http method
	 * 
	 * @param annotatedClassObjects
	 * @return Object
	 * @throws Exception
	 */
	private Object annotatedMethodProcessor(Map<String, Object> annotatedClassObjects) throws Exception {
		String handlerName = annotationProcessorConfig.getHandlerName();
		String httpMethod = annotationProcessorConfig.getHttpMethod();

		Object object = null;
		Object returnObject = null;
		Method[] declaredMethods = null;
		String annotationValue = "";
		String annotationMethod = "";
		for (String className : annotatedClassObjects.keySet()) {
			object = annotatedClassObjects.get(className);
			declaredMethods = object.getClass().getDeclaredMethods();
			for (Method method : declaredMethods) {
				if (method.isAnnotationPresent(HandlerMethod.class)) {
					annotationValue = method.getAnnotation(HandlerMethod.class).value();
					annotationMethod = method.getAnnotation(HandlerMethod.class).method();
					if (annotationValue.equals(handlerName) && annotationMethod.equals(httpMethod)) {
						returnObject = method.invoke(object, prepareArguments(method, annotatedClassObjects));
						System.out.println("Executing [" + httpMethod + " " + handlerName + "]");
					}
				}
			}
		}
		return returnObject;
	}

	/**
	 * Create the argument array for invoking a method on an object, if there in no
	 * argument then returns empty array with size 0
	 * 
	 * @param method
	 * @param annotatedClassObjects
	 * @return Object[]
	 */
	private Object[] prepareArguments(Method method, Map<String, Object> annotatedClassObjects) {
		Class<?>[] parameterTypes = method.getParameterTypes();
		Object arguments[] = new Object[parameterTypes.length];
		for (int i = 0; i < parameterTypes.length; i++) {
			arguments[i] = annotatedClassObjects.get(parameterTypes[i].getName());
		}
		return arguments;
	}
}
