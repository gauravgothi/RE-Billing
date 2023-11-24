package in.co.mpwin.rebilling.jwt.payload;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    @Id
    private Integer id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    @Size(min = 7)
    private String password;

    @Column(nullable = false)
    private String role;

    private String email;

    private String status;

    private String remark;

}
