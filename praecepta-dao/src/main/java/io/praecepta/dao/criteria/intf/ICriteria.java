package io.praecepta.dao.criteria.intf;

/**
 * @author PunugotiR
 *	Generic Criteria 
 * @param <T>
 */
public interface ICriteria<T extends ICriterion> {

	void add(T criterion);
	void update(T criterion);
	void delete(T criterion);
	void clear();
}
