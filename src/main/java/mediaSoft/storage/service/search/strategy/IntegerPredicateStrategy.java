package mediaSoft.storage.service.search.strategy;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class IntegerPredicateStrategy implements PredicateStrategy<Integer> {

  @Override
  public Predicate getEqPattern(Expression<Integer> expression, Integer value, CriteriaBuilder cb) {
    return cb.equal(expression, value);
  }

  @Override
  public Predicate getLeftLimitPattern(Expression<Integer> expression, Integer value, CriteriaBuilder cb) {
    return cb.greaterThanOrEqualTo(expression, value);
  }

  @Override
  public Predicate getRightLimitPattern(Expression<Integer> expression, Integer value, CriteriaBuilder cb) {
    return cb.lessThanOrEqualTo(expression, value);
  }

  @Override
  public Predicate getLikePattern(Expression<Integer> expression, Integer value, CriteriaBuilder cb) {
    int lowerBound = (int) Math.round(value * 0.9);
    int upperBound = (int) Math.round(value * 1.1);

    return cb.and(
            cb.greaterThanOrEqualTo(expression, lowerBound),
            cb.lessThanOrEqualTo(expression, upperBound)
    );
  }
}
