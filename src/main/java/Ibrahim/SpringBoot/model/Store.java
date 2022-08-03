package Ibrahim.SpringBoot.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Stores")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name, address,Password;
    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY,
            cascade = CascadeType.REMOVE,targetEntity = Product.class)
    private List<Product> Products;
    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY,
            cascade = CascadeType.REMOVE,targetEntity = Agent.class)
    private List<Agent> Agents;
    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY,
            cascade = CascadeType.REMOVE,targetEntity = Agent.class)
    private List<Bill> Bills;
}
