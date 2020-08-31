package hr.fer.zemris.java.hw05.db;

import java.util.List;
import java.util.Objects;

/**
 * Models a simple database query filter that checks if all conditional expressions
 * are satisfied in order to determine if a record should be included or not.
 * 
 * @author Ivan Skorupan
 */
public class QueryFilter implements IFilter {
	
	/**
	 * A list of conditional expressions used for filtering.
	 */
	private List<ConditionalExpression> conditionalExpressions;
	
	/**
	 * Constructs a new {@link QueryFilter} object.
	 * 
	 * @param conditionalExpressions - list of conditional expressions used for filtering
	 * @throws NullPointerException if <code>conditionalExpressions</code> is <code>null</code>
	 */
	public QueryFilter(List<ConditionalExpression> conditionalExpressions) {
		this.conditionalExpressions = Objects.requireNonNull(conditionalExpressions);
	}
	
	@Override
	public boolean accepts(StudentRecord record) {
		for(ConditionalExpression condExpr : conditionalExpressions) {
			if(!condExpr.getComparisonOperator().satisfied(condExpr.getFieldGetter().get(record), condExpr.getStringLiteral())) {
				return false;
			}
		}
		
		return true;
	}
	
}
