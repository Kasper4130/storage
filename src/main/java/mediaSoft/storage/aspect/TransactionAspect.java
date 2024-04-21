package mediaSoft.storage.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import java.text.DecimalFormat;

@Aspect
@Component
public class TransactionAspect {

  private final ThreadLocal<Long> startTime = new ThreadLocal<>();

  @Before("@annotation(mediaSoft.storage.annotation.MeasureExecutionTime)")
  public void beforeTransactionExecution() {
    startTime.set(System.nanoTime());
  }

  public void afterTransactionExecution() {
    long duration = System.nanoTime() - startTime.get();
    double seconds = duration / 1_000_000_000.0; // перевод наносекунд в секунды
    DecimalFormat df = new DecimalFormat("0.###"); // форматирование до трех знаков после точки
    System.out.println("Transaction execution time: " + df.format(seconds) + " seconds");
    startTime.remove();
  }

  @AfterReturning("@annotation(mediaSoft.storage.annotation.MeasureExecutionTime)")
  public void afterSchedulerExecution(JoinPoint joinPoint) {

    if (TransactionSynchronizationManager.isActualTransactionActive()) {
      TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {

        @Override
        public void beforeCommit(boolean readOnly) {
          beforeTransactionExecution();
        }

        @Override
        public void afterCommit() {
          afterTransactionExecution();
        }
      });
    } else {
      afterTransactionExecution();
    }
  }
}