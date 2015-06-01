package vn.itt.utils.converter;

import java.util.Map;

public interface JSONObjectGenerator <V> {
	public Class<V> getGeneratorClass();
	public Map <String, Object> createJSONObject(V item);
}
