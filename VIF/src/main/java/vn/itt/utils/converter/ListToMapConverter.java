package vn.itt.utils.converter;

public interface ListToMapConverter<V> {
    public long getKey(V item);
    public String getValue(V item);
    public abstract Class<V> getConverterClass();
}