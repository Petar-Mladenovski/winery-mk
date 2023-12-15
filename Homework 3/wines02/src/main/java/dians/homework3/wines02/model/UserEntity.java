package dians.homework3.wines02.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String username;
    private String email;
    private String password;
    private String phoneNumber;
    private String address;
    private String photoUrl;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "users_role",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "Id")}
    )
    private List<Role> roles = new ArrayList<>();

    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Event> events = new ArrayList<>();

    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(FetchMode.SELECT)
    private List<Order> orders = new ArrayList<>();

    @OneToOne(mappedBy = "createdBy", cascade = CascadeType.ALL, orphanRemoval = true)
    private Cart cart = null;

    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(FetchMode.SELECT)
    private List<EventComment> comments = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime createdOn;
    @UpdateTimestamp
    private LocalDateTime updatedOn;
}