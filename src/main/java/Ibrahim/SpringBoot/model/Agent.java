package Ibrahim.SpringBoot.model;


import Ibrahim.SpringBoot.annotation.FieldsValueMatch;
import Ibrahim.SpringBoot.annotation.PasswordValidator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name="Agents")
@Data
@FieldsValueMatch.List({
        @FieldsValueMatch(
                field = "pwd",
                fieldMatch = "confirmPwd",
                message = "Passwords do not match!"
        ),
        @FieldsValueMatch(
                field = "email",
                fieldMatch = "confirmEmail",
                message = "Email addresses do not match!"
        )
})
@NoArgsConstructor
@AllArgsConstructor
public class Agent extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotBlank(message="Name can't be blank")
    @Size(min=3, message="Name must be at least 3 characters long")
    private String name;
    @OneToOne(fetch = FetchType.EAGER,cascade = CascadeType.PERSIST, targetEntity = Role.class)
    @JoinColumn(name = "role_id", referencedColumnName = "roleId",nullable = false)
    private Role role;
    @ManyToOne( optional = true)
    @JoinColumn(name = "store_id",referencedColumnName = "id")
    private Store store;
    @NotBlank(message="Mobile number must not be blank")
    @Pattern(regexp="(^$|[0-9]{8})",message = "Mobile number must be 8 digits")
    private String mobileNumber;

    @NotBlank(message="Email must not be blank")
    @Email(message = "Please provide a valid email address" )
    @Column(unique=true)
    private String email;

    @NotBlank(message="Confirm Email must not be blank")
    @Email(message = "Please provide a valid confirm email address" )
    @Transient
    private String confirmEmail;

    @NotBlank(message="Password must not be blank")
    @Size(min=5, message="Password must be at least 5 characters long")
    @PasswordValidator
    private String pwd;

    @NotBlank(message="Confirm Password must not be blank")
    @Size(min=5, message="Confirm Password must be at least 5 characters long")
    @Transient
    private String confirmPwd;

    @OneToMany(mappedBy = "id",
            cascade = CascadeType.PERSIST,targetEntity = Bill.class)
    private List<Bill> Bills;

    private AgentStatus status=AgentStatus.AWAITING_CONFIRMATION;

    private String image="images.png";
}
