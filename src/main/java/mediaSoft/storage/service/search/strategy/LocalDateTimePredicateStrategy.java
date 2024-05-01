package mediaSoft.storage.service.search.strategy;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;

import java.time.LocalDateTime;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class LocalDateTimePredicateStrategy implements PredicateStrategy<LocalDateTime> {

  @Override
  public Predicate getEqPattern(Expression<LocalDateTime> expression, LocalDateTime value, CriteriaBuilder cb) {
    return cb.equal(cb.function("date", LocalDateTime.class, expression), value.toLocalDate());
  }

  @Override
  public Predicate getLeftLimitPattern(Expression<LocalDateTime> expression, LocalDateTime value, CriteriaBuilder cb) {
    return cb.greaterThanOrEqualTo(expression, value);
  }

  @Override
  public Predicate getRightLimitPattern(Expression<LocalDateTime> expression, LocalDateTime value, CriteriaBuilder cb) {
    return cb.lessThanOrEqualTo(expression, value);
  }

  @Override
  public Predicate getLikePattern(Expression<LocalDateTime> expression, LocalDateTime value, CriteriaBuilder cb) {
    return cb.between(expression, value.minusDays(3), value.plusDays(3));
  }
}
