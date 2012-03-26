package com.jaehan.portal.persistence;

import java.util.List;

/**
 * @author 정재한
 * 기본 CRUD Mapper
 * @param <T>
 */
public interface GenericMapper<T> {
	T add(T entity);

	T update(T entity);
	
	void delete(T entity);
	
	int deleteAll();

	T get(String id);

	List<T> getAll();
	
	List<T> search(String name);
	
	int count(T entity);
}
