package mediaSoft.storage.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import java.text.DecimalFormat;

@Aspect
@Component
public class ExecutionTimeAspect {

  private final ThreadLocal<Long> startTime = new ThreadLocal<>();

  @Before("@annotation(mediaSoft.storage.annotation.MeasureExecutionTime)")
  public void beforeMethodExecution(JoinPoint joinPoint) {
    startTime.set(System.nanoTime());
  }

  @After("@annotation(mediaSoft.storage.annotation.MeasureExecutionTime)")
  public void afterMethodExecution(JoinPoint joinPoint) {
    long duration = System.nanoTime() - startTime.get();
    double seconds = duration / 1_000_000_000.0; // перевод наносекунд в секунды
    DecimalFormat df = new DecimalFormat("0.###"); // форматирование до трех знаков после точки
    System.out.println("Method " + joinPoint.getSignature().toShortString() + " execution time: " + df.format(seconds) + " seconds");
    startTime.remove();
  }
}