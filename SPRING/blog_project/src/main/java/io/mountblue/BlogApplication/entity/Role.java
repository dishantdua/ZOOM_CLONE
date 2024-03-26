package io.mountblue.BlogApplication.entity;

import jakarta.persistence.*;

import java.util.List;


@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Column(nullable = false)
    private String name;

    @Column(name="role")
    private String role;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id =id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
