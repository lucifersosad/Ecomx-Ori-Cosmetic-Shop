package ori.model;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotEmpty; //spring boot 3.1.5

import lombok.*;

@Data

@AllArgsConstructor

@NoArgsConstructor
public class ShoppingSessionModel {
	private Integer shopSesId;
	@NotEmpty

	@Length(min=5)
	private Integer userId;
	private Integer proId;
	private Boolean isEdit = false;
}
