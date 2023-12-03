package ori.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "orderId")
	private Integer orderId;
	@Column(name = "order_date")
	private String order_date;
	@Column(name = "payment_method")
	private String payment_method;
	@Column(name = "shipping_method")
	private String shipping_method;
	@Column(name = "order_status")
	private String order_status;
	@Column(name = "order_total")
	private String total;
//	@ManyToOne
//	@JoinColumn(name="userId")
//	private User userId;
}
