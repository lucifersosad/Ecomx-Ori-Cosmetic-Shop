package ori.entity;

import java.io.Serializable;
import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name="`order`")
@AllArgsConstructor
@NoArgsConstructor
public class Order implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "orderid")
	private Integer orderId;
	@Column(name = "userid")
	private Integer userId;
	@Column(name = "order_date")
	private Date order_date;
	@Column(name="payment_method")
	private String payment_method;
	@Column(name="shipping_method")
	private String shipping_method;
	@Column(name = "order_total")
    private Integer order_total;

	@Column(name="order_status")
	private Boolean order_status;

}
