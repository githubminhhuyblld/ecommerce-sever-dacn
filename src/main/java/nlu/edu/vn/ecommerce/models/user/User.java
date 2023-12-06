package nlu.edu.vn.ecommerce.models.user;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import nlu.edu.vn.ecommerce.base.BaseEntity;
import nlu.edu.vn.ecommerce.models.address.Address;
import nlu.edu.vn.ecommerce.models.enums.GenderType;
import nlu.edu.vn.ecommerce.models.enums.ServiceType;
import nlu.edu.vn.ecommerce.models.role.Role;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.Null;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a user entity.
 */
@EqualsAndHashCode(callSuper = true)
@Document
@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class User extends BaseEntity implements UserDetails {
    /**
     * The unique identifier for the user.
     */
    @Id
    String id;

    /**
     * The unique username for the user.
     */
    @Indexed(unique = true)
    @Null
    private String username;

    /**
     * The unique email address for the user.
     */
    @Indexed(unique = true)
    @NonNull
    private String email;

    /**
     * The password for the user (JSON ignored for security).
     */
    @JsonIgnore
    @NonNull
    private String password;

    /**
     * The image URL for the user's profile picture.
     */
    private String image;

    /**
     * The first name of the user.
     */
    private String firstName;

    /**
     * The last name of the user.
     */
    private String lastName;

    /**
     * The phone number of the user.
     */
    private String numberPhone;

    /**
     * The date of birth of the user.
     */
    private long dateOfBirth;

    /**
     * The gender of the user.
     */
    private GenderType gender;

    /**
     * The type of service for the user.
     */
    private ServiceType serviceType;

    /**
     * The list of addresses associated with the user.
     */
    private List<Address> address;

    /**
     * The list of roles assigned to the user.
     */
    private List<Role> roles;

    /**
     * The ID of the shop associated with the user.
     */
    private String shopId;


    public User(String id, @NonNull String username, @NonNull String password) {
        this.id = id;
        this.username = username;
        this.password = password;

    }

    public User(String id, @NonNull String username, @NonNull String password, List<Role> roles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.roles = roles;
    }


    public User(@NonNull String username, @NonNull String email, @NonNull String password, @NonNull String firstName, @NonNull String lastName, @NonNull String numberPhone) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.numberPhone = numberPhone;
    }

    public User(@NonNull String username, @NonNull String email, @NonNull String password, @NonNull String firstName, @NonNull String lastName, @NonNull String numberPhone, @NonNull String image, GenderType gender, long birthday) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.numberPhone = numberPhone;
        this.dateOfBirth = birthday;
        this.image = image;
        this.gender = gender;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
