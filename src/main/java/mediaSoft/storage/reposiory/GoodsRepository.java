package mediaSoft.storage.reposiory;

import jakarta.persistence.LockModeType;
import mediaSoft.storage.model.Goods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.List;
import java.util.UUID;

public interface GoodsRepository extends JpaRepository<Goods, UUID> {

  Goods findByArticle(String article);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  List<Goods> findAll();
}
