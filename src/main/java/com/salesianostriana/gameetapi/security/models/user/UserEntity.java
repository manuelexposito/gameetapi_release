package com.salesianostriana.gameetapi.security.models.user;

import com.salesianostriana.gameetapi.models.requests.Request;
import com.salesianostriana.gameetapi.models.sessions.Session;
import com.salesianostriana.gameetapi.security.models.user.role.UserRole;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Parameter;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Past;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;


@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity implements UserDetails, Serializable {


    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator",
            parameters = {
                    @Parameter(
                            name = "uuid_gen_strategy_class",
                            value = "org.hibernate.id.uuid.CustomVersionOneStrategy"
                    )
            }
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @NaturalId
    @Column(unique = true, updatable = false)
    private String username;

    @NaturalId
    @Column(unique = true, updatable = false)
    private String email;

    private String password;

    @Past
    private LocalDate birthdate;

    private String fullName;

    @Length(max = 250)
    private String biography;

    @Builder.Default
    private String[] interests = {};

    private String avatar;

    //PLANNED_SESSIONS
    @Builder.Default
    @ManyToMany
    @JoinTable(joinColumns = @JoinColumn(name = "player_id",
            foreignKey = @ForeignKey(name = "FK_SESSION_PLAYER")),
            inverseJoinColumns = @JoinColumn(name = "session_id",
                    foreignKey = @ForeignKey(name = "FK_PLAYER_SESSION")),
            name = "session_players"
    )
    private List<Session> sessions = new ArrayList<>();


    private String latlong;
    //votesRecibidos

    //FRIENDS
    @Builder.Default
    @ManyToMany
    @JoinTable(joinColumns = @JoinColumn(name = "user1_id",
            foreignKey = @ForeignKey(name = "FK_user1_user2_FRIENDSHIP")),
            inverseJoinColumns = @JoinColumn(name = "user2_id",
                    foreignKey = @ForeignKey(name = "FK_user2_user1_FRIENDSHIP")),
            name = "friendship"
    )
    private List<UserEntity> friends = new ArrayList<>();


    //REQUESTS
    @Builder.Default
    @OneToMany(mappedBy = "userRequested")
    private List<Request> requests = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private UserRole role;


    @Builder.Default
    @CreatedDate
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    private LocalDateTime lastPasswordChangeAt = LocalDateTime.now();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
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


    //HELPERS

    //SESSION
    public void addToSession(Session s) {

        s.getPlayers().add(this);
        getSessions().add(s);


    }

    public void removeFromSession(Session s) {
        s.getPlayers().remove(this);
        getSessions().remove(s);

    }


    public void addFriend(UserEntity user){
        user.getFriends().add(this);
        this.getFriends().add(user);
    }

    public void removeFriend(UserEntity user){
        user.getFriends().remove(this);
        this.getFriends().remove(user);
    }

    //REQUESTS (userRequested)

    public void addRequest(Request request){
        getRequests().add(request);
        request.setUserRequested(this);
    }
    public void removeRequest(Request request){
        getRequests().remove(request);
        request.setUserRequested(null);
    }


}
