package psn.mapper;

import java.util.List;
import java.util.Map;

public interface BaseMapper<T> {

	public abstract void create(T bean);
	
	public abstract void delete(T bean);
	
	public abstract void update(T bean);
	
	public abstract List<T> retrieveAll();
	
	public abstract T retrieveById(T bean);

	public abstract List<T> pagination(Map<String, Integer> range);

	public abstract Integer retrieveSize();
}
