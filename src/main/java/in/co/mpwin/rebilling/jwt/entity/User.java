package in.co.mpwin.rebilling.jwt.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "re_login")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    @Size(min = 7)
    private String password;

    @Column(nullable = false)
    private String role;

    private String status;

    private String remark;

    private boolean enabled;

    private String email;

    @NotNull
    private String createdBy;
    @NotNull
    private String updatedBy;
    @NotNull
    private Timestamp createdOn;
    @NotNull
    private Timestamp updatedOn;

    @Column(name = "reset_password_token")
    private String resetPasswordToken;

}
