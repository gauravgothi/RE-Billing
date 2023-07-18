package in.co.mpwin.rebilling.jwt.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
    private Integer id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    @Size(min = 8)
    private String password;

    @Column(nullable = false)
    private String role;

    private String status;

    private String remark;

    @NotNull
    private String createdBy;
    @NotNull
    private String updatedBy;
    @NotNull
    private Timestamp createdOn;
    @NotNull
    private Timestamp updatedOn;

}
