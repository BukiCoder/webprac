package web_prak.models;

import lombok.*;

import jakarta.persistence.*;

@Entity
@Table(name = "manager")
@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class Manager implements CommonEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    @NonNull
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    @NonNull
    private String email;

    @Column(name = "phone", nullable = false, unique = true)
    @NonNull
    private String phone;

    @Column(name = "password_hash", nullable = false)
    @NonNull
    private String passwordHash;
}