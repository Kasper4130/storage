package mediaSoft.storage.reposiory;

import mediaSoft.storage.model.Goods;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GoodsRepository extends JpaRepository<Goods, UUID> {

  Goods findByArticle(String article);
}
