package mediaSoft.storage.reposiory;

import mediaSoft.storage.model.Goods;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface GoodsRepository extends JpaRepository<Goods, UUID>, JpaSpecificationExecutor<Goods> {

  Goods findByArticle(String article);

  Page<Goods> findAll(Pageable pageable);
}
