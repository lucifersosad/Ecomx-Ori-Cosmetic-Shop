package ori.model;




import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@AllArgsConstructor

@NoArgsConstructor
public class PaymentModel {
	private Integer payId;

	private Long amount;
	
	private Integer cardType;
	
	private Integer orderInfo;
	
	private Integer payDate;
	
	private Integer responseCode;

	private Integer tmnCode;
	
	private Integer transactionNo;
	
	private Integer transactionStatus;

	private Integer txnRef;

	private Integer secureHash;
}
