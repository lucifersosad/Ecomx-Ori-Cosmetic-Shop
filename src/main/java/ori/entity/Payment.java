package ori.entity;

import java.io.Serializable;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "payid")
	private Integer payId;
	@Column(name = "amount")
	private Long amount;
	@Column(name = "orderinfo")
	private String orderInfo;
	@Column(name = "paydate")
	private String payDate;
	@Column(name = "paystatus")
	private boolean payStatus;
//	@OneToOne
//	@JoinColumn(name = "orderId")
//	 private Order order;
}
