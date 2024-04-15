package mediaSoft.storage.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.hibernate.Hibernate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents goods table from SQL database
 */
@Entity
@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
@Table(name = "goods")
public class Goods {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @NotBlank
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank
    @Column(name = "article", nullable = false, unique = true)
    private String article;

    @Column(name = "description")
    private String description;

    @NotBlank
    @Column(name = "good_category", nullable = false)
    private String goodCategory;

    @PositiveOrZero
    @Column(name = "price", scale = 2, nullable = false)
    private BigDecimal price;

    @PositiveOrZero
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "last_quantity_change")
    private LocalDateTime lastQuantityChange;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Goods goods = (Goods) o;
        return getId() != null && Objects.equals(getId(), goods.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
