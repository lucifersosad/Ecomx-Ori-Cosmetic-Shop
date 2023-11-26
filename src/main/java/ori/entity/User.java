package ori.entity;

import java.io.Serializable;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="user")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="userid")
	private Integer userId;
	@Column(name="password")
	private String password;
	@Column(name="fullname")
	private String fullName;
	@Column(name="email")
	private String email;
	@Column(name="phone")
	private String phone;
	@Column(name="address")
	private String address;
	@Column(name="is_admin")
	private Boolean isAdmin;
	
}
