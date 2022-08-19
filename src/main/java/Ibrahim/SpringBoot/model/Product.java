package Ibrahim.SpringBoot.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "Products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @DecimalMin(value = "1",message="Quantity must be > 0")
    private int quantity;
    @DecimalMin(value = "0.001",message="Price must be > 0")
    private float price;
    @NotBlank(message="Name must not be blank")
    @Size(min = 3,message = "Name must be at least 3 characters long")
    private String name ;

    @NotBlank(message="Reference must not be blank")
    @Size(min = 3,message = "Reference must be at least 4 characters long")
    private String reference ;
    @NotBlank(message="Categorie must not be blank")
    @Size(min = 3,message = "Categorie must be at least 3 characters long")
    private String categories;
    @ManyToOne( optional = false)
    @JoinColumn(name = "store_id",referencedColumnName = "id")
    private Store store;
    @ManyToOne(optional = true)
    @JoinColumn(name = "bill",referencedColumnName = "id")
    private Bill bill;

  /*  public Product() {
    }

    public Product(Long id, int quantity, float price, String name, String categories) {
        this.id = id;
        this.quantity = quantity;
        this.price = price;
        this.name = name;
        this.categories = categories;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }*/
}
