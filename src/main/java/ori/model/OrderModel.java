package ori.model;

import java.sql.Date;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@AllArgsConstructor

@NoArgsConstructor

public class OrderModel {
	private Integer orderId;



	private Integer userId;

	private Date order_date;

	private String payment_method;

	private String shipping_method;
	
	private Boolean order_status;
	
	private Integer order_total;
	
	private Boolean isEdit = false;

}
