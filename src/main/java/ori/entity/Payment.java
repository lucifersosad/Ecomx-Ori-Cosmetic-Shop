package ori.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
	@Column(name = "cardtype")
	private Integer cardType;
	@Column(name = "orderinfo")
	private Integer orderInfo;
	@Column(name = "paydate")
	private Integer payDate;
	@Column(name = "responsecode")
	private Integer responseCode;
	@Column(name = "tmncode")
	private Integer tmnCode;
	@Column(name = "transactionno")
	private Integer transactionNo;
	@Column(name = "transactionstatus")
	private Integer transactionStatus;
	@Column(name = "txnRef")
	private Integer txnRef;
	@Column(name = "securehash")
	private Integer secureHash;
}
