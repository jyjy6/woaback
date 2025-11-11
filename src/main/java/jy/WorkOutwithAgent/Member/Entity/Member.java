package jy.WorkOutwithAgent.Member.Entity;


import jakarta.persistence.*;
import jy.WorkOutwithAgent.Meal.Entity.Meal;
import jy.WorkOutwithAgent.Workout.Entity.Workout;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    @Column(unique = true, nullable = false, updatable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String displayName;

    private String phone;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String sex;
    private Integer age;
    private Double height;
    private Double weight;



    private LocalDateTime lastLogin;
    private Integer loginAttempts;
    private LocalDateTime loginSuspendedTime;

    private boolean termsAccepted;
    private boolean privacyAccepted = false;
    private boolean marketingAccepted = false;

    private boolean isSuperAdmin = false;
    private boolean isPremium = false;

    private LocalDateTime premiumExpiryDate;


    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "member_roles", joinColumns = @JoinColumn(name = "member_id"))
    @Column(name = "role")
    @Builder.Default
    private Set<String> roles = new HashSet<>(Set.of("ROLE_USER")); // 기본 역할

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Meal> meals = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Workout> workouts = new ArrayList<>();



    /*
     * roles 헬퍼함수들
     */
    public boolean hasRole(String role) {
        return roles.contains(role);
    }
    public void addRole(String role) {
        roles.add(role);
    }
    public void removeRole(String role) {
        roles.remove(role);
        if (roles.isEmpty()) {
            roles.add("ROLE_USER");
        }
    }
    public Set<String> getRoleSet() {
        return new HashSet<>(roles);
    }
    
    
    
    
}


