package vn.vif.utils.converter;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;
import java.util.Map;

public class JSONObjectComparator implements Comparator<Map<String, Object>> {
	private Collator vnCollator=Collator.getInstance(new Locale("vi"));
	
	public int compare(Map<String, Object> m1, Map<String, Object> m2) {
		return vnCollator.compare((String) m1.get("name"), (String) m2.get("name"));
	}

}
