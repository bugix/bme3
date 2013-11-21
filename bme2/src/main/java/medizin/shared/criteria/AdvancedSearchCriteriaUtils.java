package medizin.shared.criteria;

import java.util.ArrayList;
import java.util.List;

import medizin.shared.utils.SharedConstant;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.base.Objects.ToStringHelper;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;

public class AdvancedSearchCriteriaUtils {
	
	public static String encode(AdvancedSearchCriteria advancedSearchCriteria)
	{
		ToStringHelper helper = Objects.toStringHelper("");
		helper.add(SharedConstant.POSSIBLE_FIELD, advancedSearchCriteria.getPossibleFields());
		helper.add(SharedConstant.BINDTYPE, advancedSearchCriteria.getBindType());
		helper.add(SharedConstant.COMPARISON, advancedSearchCriteria.getComparison());
		helper.add(SharedConstant.FIELDVALUE, advancedSearchCriteria.getValue());
		return helper.toString();		
	}
	
	public static List<String> encodeList(List<AdvancedSearchCriteria> advancedSearchCriteriaList)
	{ 
		List<String> encodedStringList = new ArrayList<String>();
		for (AdvancedSearchCriteria criteria : advancedSearchCriteriaList)
		{
			encodedStringList.add(encode(criteria));
		}
		
		return encodedStringList;
	}
	
	public static AdvancedSearchCriteria decode(String encodedString)
	{
		encodedString = encodedString.replace("{", "").replace("}", "");
		//FluentIterable<String> fluentIterable = FluentIterable.from(Splitter.on(",").omitEmptyStrings().trimResults().split(encodedString));
		AdvancedSearchCriteria advancedSearchCriteria = new AdvancedSearchCriteria();
		advancedSearchCriteria.setBindType(parseBindType(encodedString));
		advancedSearchCriteria.setComparison(parseComparison(encodedString));
		advancedSearchCriteria.setPossibleFields(parsePossibleFields(encodedString));
		advancedSearchCriteria.setValue(parseValue(encodedString));
		return advancedSearchCriteria;
	}
	
	private static String parseValue(String encodedString) {
		int start = encodedString.indexOf(SharedConstant.FIELDVALUE);
		int end = encodedString.length();
		String value = encodedString.substring(start,end);
		
		if(value != null && value.isEmpty() == false) {
			start = value.indexOf("=");
			if(start>0 && start +1 < value.length()) {
				return value.substring(start+1).trim();
			}
		}
		return "";
	}

	private static PossibleFields parsePossibleFields(String encodedString) {
		int start = encodedString.indexOf(SharedConstant.POSSIBLE_FIELD);
		int end = encodedString.indexOf(",",start);
		String value = encodedString.substring(start,end);
		
		if(value != null && value.isEmpty() == false) {
			start = value.indexOf("=");
			if(start>0 && start +1 < value.length()) {
				return PossibleFields.valueOf(value.substring(start+1).trim());
			}
		}
		return null;
	}

	private static Comparison parseComparison(String encodedString) {
		int start = encodedString.indexOf(SharedConstant.COMPARISON);
		int end = encodedString.indexOf(",",start);
		String value = encodedString.substring(start,end);
		
		if(value != null && value.isEmpty() == false) {
			start = value.indexOf("=");
			if(start>0 && start +1 < value.length()) {
				return Comparison.valueOf(value.substring(start+1).trim());
			}
		}
		return null;
	}

	private static BindType parseBindType(String encodedString) {
		int start = encodedString.indexOf(SharedConstant.BINDTYPE);
		int end = encodedString.indexOf(",",start);
		String value = encodedString.substring(start,end);
		
		if(value != null && value.isEmpty() == false) {
			start = value.indexOf("=");
			if(start>0 && start +1 < value.length()) {
				return BindType.valueOf(value.substring(start+1).trim());
			}
		}
		return null;
	}

	public static List<AdvancedSearchCriteria> decodeList(List<String> decodedStringList)
	{
		List<AdvancedSearchCriteria> criteriaList = new ArrayList<AdvancedSearchCriteria>();
		
		for (String decodeString : decodedStringList)
		{
			criteriaList.add(decode(decodeString));
		}
		
		return criteriaList;
	} 
	
	private static PossibleFields parsePossibleFields(FluentIterable<String> fluentIterable) {
		String value = parseValueToString(fluentIterable,possibleFieldsPredicate);
		return PossibleFields.valueOf(value);
	}

	private static Comparison parseComparison(FluentIterable<String> fluentIterable) {
		String value = parseValueToString(fluentIterable,comparisonPredicate);
		return Comparison.valueOf(value);
	}

	private static String parseValue(FluentIterable<String> fluentIterable) {
		return parseValueToString(fluentIterable,fieldValuePredicate);
	}

	private static BindType parseBindType(FluentIterable<String> fluentIterable) {
		String value = parseValueToString(fluentIterable,bindTypePredicate);
		return BindType.valueOf(value);
	}

	private static String parseValueToString(FluentIterable<String> fluentIterable,Predicate<String> predicate) {
		FluentIterable<String> iterable = fluentIterable.filter(predicate).transform(splitToValues);
		if(iterable != null && !iterable.isEmpty()) {
			return iterable.get(0);
		}
		else { 
			return null;
		}
	}
	
	private static final Predicate<String> possibleFieldsPredicate =  new Predicate<String>() {

		@Override
		public boolean apply(String input) {
			return input.contains(SharedConstant.POSSIBLE_FIELD);
		}
	};

	private static final Predicate<String> comparisonPredicate =  new Predicate<String>() {

		@Override
		public boolean apply(String input) {
			return input.contains(SharedConstant.COMPARISON);
		}
	};
	private static final Predicate<String> bindTypePredicate =  new Predicate<String>() {

		@Override
		public boolean apply(String input) {
			return input.contains(SharedConstant.BINDTYPE);
		}
	};
	private static final Predicate<String> fieldValuePredicate =  new Predicate<String>() {

		@Override
		public boolean apply(String input) {
			return input.contains(SharedConstant.FIELDVALUE);
		}
	};
	private static final Function<String, String> splitToValues = new Function<String, String>() {
		
		@Override
		public String apply(String input) {
		
			if(input == null || input.isEmpty()){
				return null;
			}
			else {
				List<String> list = Lists.newArrayList(Splitter.on("=").omitEmptyStrings().trimResults().split(input));
				if(list.size() == 2) {
					return list.get(1);
				}else {
					return null;
				}
				
			}
		}
	};
}
