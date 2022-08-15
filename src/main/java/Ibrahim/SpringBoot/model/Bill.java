package Ibrahim.SpringBoot.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Bill extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Name can't be blank")
    @Size(min = 3, message = "Name must be at least 3 characters long")
    private String client;

    private String status;

    private float total = 0;
    @OneToMany(mappedBy = "id", fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST, targetEntity = Product.class)
    private List<Product> Products;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "agent", referencedColumnName = "id")
    private Agent agent;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "store", referencedColumnName = "id")
    private Store store;
}
