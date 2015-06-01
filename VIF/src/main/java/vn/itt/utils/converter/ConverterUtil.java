package vn.itt.utils.converter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConverterUtil {
	public static <V> Map<Long, String> convertListToMap(Collection<V> sourceList,
			ListToMapConverter<V> converter) {
		Map<Long, String> resultMap = new HashMap<Long, String>();
		if (sourceList!=null) {//chống NullPointerException
			for (V item : sourceList) {
				resultMap.put(converter.getKey(item), converter.getValue(item));
			}
		}
		return resultMap;
	}
	
	public static <V> List<Map<String, Object>> convertToJSONList(Collection<V> sourceList,
			JSONObjectGenerator<V> generator) {
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		for (V item : sourceList) {
			resultList.add(generator.createJSONObject(item));
		}
		Collections.sort(resultList, new JSONObjectComparator());
		return resultList;
	}
}
