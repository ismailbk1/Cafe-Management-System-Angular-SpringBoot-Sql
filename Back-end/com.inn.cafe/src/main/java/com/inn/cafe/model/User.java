package com.inn.cafe.model;

import jakarta.persistence.*;
import lombok.*;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@NamedQuery(name="User.findByEmailId" , query = "select u from User u where email=:email")

@NamedQuery(name="User.getAllUser" , query = "select new com.inn.cafe.wrapper.UserWrapper(u.id,u.name,u.email,u.contactNumber,u.status) from User u where u.role='user'")


@NamedQuery(name="User.updateStatus" , query = "update User u set u.status=:status where u.id=:id")
@NamedQuery(name="User.getAllAdmin" , query = "select u.email from User u where u.role='admin'")

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "user")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable , UserDetails {


    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
   @Id
    private Integer id;
    @Column(name="name")
    private String name;
    @Column(name="contactNumber")
    private String contactNumber;
    @Column(name="email")
    private String email;

    @Column(name="password")
    private String password;
    @Column(name="status")
    private String status;

    @Column(name="role")
    private String role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(getRole()));
    }

    @Override
    public String getUsername() {
        return email;
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
