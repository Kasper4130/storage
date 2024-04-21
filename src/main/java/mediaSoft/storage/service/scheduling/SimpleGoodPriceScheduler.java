package mediaSoft.storage.service.scheduling;

import lombok.RequiredArgsConstructor;
import mediaSoft.storage.annotation.MeasureExecutionTime;
import mediaSoft.storage.model.Goods;
import mediaSoft.storage.reposiory.GoodsRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Component
@Profile("!local")
@ConditionalOnExpression(value = "#{'${app.scheduling.mode:none}'.equals('simple')}")
@RequiredArgsConstructor
public class SimpleGoodPriceScheduler {

  private final GoodsRepository goodsRepository;

  @Value("#{new java.math.BigDecimal(\"${app.scheduling.priceIncreasePercentage::10}\")}")
  private BigDecimal priceIncreasePercentage;

  @Transactional
  @MeasureExecutionTime
  @Scheduled(fixedRateString = "${app.scheduling.period}")
  public void increaseGoodPrice() {

    System.out.println("Start Simple ...");

    final List<Goods> goodsList = goodsRepository.findAll();
    goodsList.forEach(good -> good.setPrice(getNewPrice(good.getPrice(), priceIncreasePercentage)));
    goodsRepository.saveAll(goodsList);
  }

  private BigDecimal getNewPrice(BigDecimal oldPrice, BigDecimal increase) {
    return oldPrice.multiply(BigDecimal.ONE.add(increase.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP)));
  }
}