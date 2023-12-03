package ori.model;

import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Data

@AllArgsConstructor

@NoArgsConstructor
public class UserModel {
	private Integer userId;

	private String password;
	private String fullName;
	private String email;
	private String phone;
	private String address;
	private Boolean active;
	private String username;
	
//	private MultipartFile imageFile;
	private Boolean isEdit=false;
	private String code;
}
