package Ibrahim.SpringBoot.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
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

    private String client;
    private String status;
    private float total=0;
    @OneToMany(mappedBy = "id", fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST,targetEntity = Product.class)
    private List<Product> Products;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "agent",referencedColumnName = "id")
    private Agent agent;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "store",referencedColumnName = "id")
    private Store store;
}
