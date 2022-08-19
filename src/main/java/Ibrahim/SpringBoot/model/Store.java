package Ibrahim.SpringBoot.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
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
    @NotBlank(message="Name must not be blank")
    private String storeName;
    @NotBlank(message="Address must not be blank")
    private String  address;
    @NotBlank(message="Mobile number must not be blank")
    @Pattern(regexp="(^$|[0-9]{8})",message = "Mobile number must be 8 digits")
    private String storeNumber;

    @OneToMany(mappedBy = "store",fetch = FetchType.EAGER,
            cascade = CascadeType.REMOVE,targetEntity = Product.class)
    private List<Product> Products;
    @OneToMany(mappedBy = "store",
            cascade = CascadeType.REMOVE,targetEntity = Agent.class)
    private List<Agent> Agents;
    @OneToMany(mappedBy = "store",
            cascade = CascadeType.REMOVE,targetEntity = Agent.class)
    private List<Bill> Bills;
}
