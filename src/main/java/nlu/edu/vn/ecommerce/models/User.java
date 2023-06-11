package nlu.edu.vn.ecommerce.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import nlu.edu.vn.ecommerce.base.BaseEntity;
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

@EqualsAndHashCode(callSuper = true)
@Document
@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class User extends BaseEntity implements UserDetails {
    @Id
    String id;
    @Indexed(unique = true)
    @Null
    private String username;
    @Indexed(unique = true)
    @NonNull
    private String email;
    @JsonIgnore
    @NonNull
    private String password;

    private String image;

    private String firstName;

    private String lastName;

    private String numberPhone;

    private String dateOfBirth;

    private GenderType gender;

    private ServiceType serviceType;

    private List<Role> roles;

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

    public User(@NonNull String username, @NonNull String email, @NonNull String password, @NonNull String firstName, @NonNull String lastName, @NonNull String numberPhone, @NonNull String image,GenderType gender,String birthday) {
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
