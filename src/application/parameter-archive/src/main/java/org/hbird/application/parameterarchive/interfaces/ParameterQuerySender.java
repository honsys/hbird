package org.hbird.application.parameterarchive.interfaces;

public interface ParameterQuerySender {

	/**
	 * Accepts a String representing a query in your database implementation. The concrete {@link ParameterQuerySender}
	 * implementation will deal with building it's actual query frmo this string.
	 * 
	 * @param dbQuery
	 * 
	 * @return results, probably a list. Depends upon your database.
	 */
	Object query(String dbQuery);

	/**
	 * Accepts an Object representing a query in your database implementation.
	 * 
	 * @param dbQuery
	 * @return results, probably a list. Depends upon your database.
	 */
	Object query(Object dbQuery);

	/**
	 * FIXME Mongo parameters in interface. They are generic concepts though, I think.
	 * 
	 * @param dbQuery
	 * @param limit
	 * @param skip
	 * @return
	 */
	Object query(Object dbQuery, int limit, int skip);

	long queryNumRecords();
}
