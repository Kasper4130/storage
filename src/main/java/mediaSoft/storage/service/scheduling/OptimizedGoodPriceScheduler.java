package mediaSoft.storage.service.scheduling;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediaSoft.storage.annotation.MeasureExecutionTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

@Slf4j
@Component
@Profile("!local")
@ConditionalOnExpression(value = "#{'${app.scheduling.mode:none}'.equals('optimized')}")
@RequiredArgsConstructor
public class OptimizedGoodPriceScheduler {

  private final DataSource dataSource;

  @Value("${app.scheduling.priceIncreasePercentage}")
  private BigDecimal priceIncreasePercentage;

  @Value("${app.scheduling.optimization.exclusive-lock}")
  private boolean exclusiveLockEnable;

  @Value("${app.scheduling.logFile}")
  private String logFilePath;

  @MeasureExecutionTime
  @Scheduled(fixedRateString = "${app.scheduling.period}")
  public void increaseGoodPrice() {

    log.info("Optimized Scheduler started");

    try (Connection connection = dataSource.getConnection();
         FileWriter writer = new FileWriter(logFilePath, true)) {
      connection.setAutoCommit(false);

      if (exclusiveLockEnable) {
        try (PreparedStatement lockStatement = connection.prepareStatement("LOCK TABLE goods IN EXCLUSIVE MODE")) {
          log.info("LOCK TABLE goods IN EXCLUSIVE MODE");
          lockStatement.executeUpdate();
          log.info("lock acquired");
        }
      }

      try (PreparedStatement selectStatement = connection.prepareStatement("SELECT * FROM goods FOR UPDATE")) {
        log.info("SELECT * FROM goods FOR UPDATE");
        try (ResultSet resultSet = selectStatement.executeQuery()) {
          while (resultSet.next()) {
            BigDecimal oldPrice = resultSet.getBigDecimal("price");
            BigDecimal newPrice = getNewPrice(oldPrice, priceIncreasePercentage);

            writer.write(buildLogMessage(resultSet));

            try (PreparedStatement updateStatement = connection.prepareStatement("UPDATE goods SET price = ? WHERE id = ?")) {
              updateStatement.setBigDecimal(1, newPrice);
              updateStatement.setObject(2, resultSet.getObject("id"));
              updateStatement.executeUpdate();
            }
          }
        }
      }
      connection.commit();
      writer.flush();

    } catch (SQLException e) {
      log.error("Unable to acquire connection", e);
      throw new RuntimeException(e);

    } catch (IOException e) {
      log.error("Error writing to file: " + logFilePath, e);
    }
  }

  private BigDecimal getNewPrice(BigDecimal oldPrice, BigDecimal increase) {
    return oldPrice.multiply(BigDecimal.ONE.add(increase.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP)));
  }

  private String buildLogMessage(ResultSet resultSet) throws SQLException {

    StringBuilder builder = new StringBuilder();
    builder.append(LocalDateTime.now()).append(": ");
    builder.append("ID: ").append(resultSet.getString("id")).append(", ");
    builder.append("Name: ").append(resultSet.getString("name")).append(", ");
    builder.append("Article: ").append(resultSet.getString("article")).append(", ");
    builder.append("Description: ").append(resultSet.getString("description")).append(", ");
    builder.append("Good Category: ").append(resultSet.getString("good_category")).append(", ");
    builder.append("Price: ").append(resultSet.getBigDecimal("price")).append(", ");
    builder.append("Quantity: ").append(resultSet.getInt("quantity")).append("\n");

    return builder.toString();
  }
}