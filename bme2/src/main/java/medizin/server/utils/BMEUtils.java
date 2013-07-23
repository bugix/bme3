package medizin.server.utils;

import ij.ImagePlus;
import ij.io.Opener;

import java.io.File;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.util.ReflectionUtils;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.web.bindery.requestfactory.server.RequestFactoryServlet;

public final class BMEUtils {
	private static Logger log = Logger.getLogger(BMEUtils.class);
	
	private BMEUtils() {
	}

	/*
	 * only useful in domain class. Cannot be used in servlet directly.
	 */
	public static String getRealPath(String path) {

		return getRealPath(RequestFactoryServlet.getThreadLocalRequest(), path);
	}
	
	/*
	 * only useful in domain class. Cannot be used in servlet directly.
	 */
	public static String getContextPath(String path) {
		return getContextPath(RequestFactoryServlet.getThreadLocalRequest(), path);
	}
	

	/*
	 * Only useful in sevlet class.
	 */
	public static String getRealPath(HttpServletRequest request, String path) {

		//String fileSeparator = System.getProperty("file.separator");
		return request.getSession().getServletContext().getRealPath(path);
	}

	/*
	 * Only useful in sevlet class.
	 */
	public static String getContextPath(HttpServletRequest request, String path) {
		String contextFileSeparator = "/";
		return request.getSession().getServletContext().getContextPath()
				+ contextFileSeparator + path;
	}

	public static int[] findImageWidthAndHeight(File appUploadedFile) {
		int[] size = null;
		try{
			ImagePlus image;
			String inputURL = "file:///" + appUploadedFile.getAbsolutePath();
			Opener opener = new Opener();
			image = opener.openURL(inputURL);
			size = new int[2];
			size[0] = image.getWidth();
			size[1] = image.getHeight();
		}catch(Exception e){
			log.error(e);
		}
		return size;
	}

	public static final Predicate<Method> getMethodFilter = new Predicate<Method>() {

		@Override
		public boolean apply(Method method) {
			 if(!method.getName().startsWith("get"))      
				 return false;
			  if(method.getParameterTypes().length != 0)   
				  return false;  
			  if(void.class.equals(method.getReturnType())) 
					  return false;
			  if(method.getName().equals("getId") || method.getName().equals("getVersion") || method.getName().equals("getClass")) {
				  return false;
			  }
			  return true;
		}
	};
	
	
	public static <T> void copyValues(final T copyFrom,final T copyTo,Class<?> leafClass) {
		Method[] methods = ReflectionUtils.getUniqueDeclaredMethods(leafClass);
		FluentIterable<Method> methodIterable = FluentIterable.from(Lists.newArrayList(methods));
		FluentIterable<Method> onlyGetMethods = methodIterable.filter(getMethodFilter);
		
		for (Method method : onlyGetMethods) {

			final String name = "set" + method.getName().substring(3);
			Optional<Method> optional = methodIterable.firstMatch(new Predicate<Method>() {

				@Override
				public boolean apply(Method input) {
					return input.getName().equals(name);
				}
			});

			if(optional.isPresent() == false) {
				log.error("Method is null");
				log.error("Name is : " + name);
			}else {
				try {
					Object obj = method.invoke(copyFrom);
					optional.get().invoke(copyTo, obj);	
				} catch (Exception e) {
					log.error("Error in object copy", e);
				}
			}
		}
	}

	public static Map<String, String> convertToMap(List<String> values) {
		Map<String, String> map = Maps.newHashMap();		
		
		if(values != null && values.size() % 2 == 0){
			for (int i=0;i<values.size();i+=2) {
				map.put(values.get(i), values.get(i+1));
			}	
		}else {
			log.error("List is not properly added");
		}
		
		return map;
	}

	public static boolean compareTwoList(List<Integer> firstList, List<Integer> secondList) {
		
		int minSize = Math.min(firstList.size(), secondList.size());
		for(int i=0;i<minSize;i++) {
			if(firstList.get(i).equals(secondList.get(i)) == false) {
				return false;
			}
		}
		
		return true;
	}
}
