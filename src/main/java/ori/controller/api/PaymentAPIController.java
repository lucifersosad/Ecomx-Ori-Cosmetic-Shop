package ori.controller.api;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.ArrayList;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import ori.config.Config;
import ori.model.Response;


@RestController
@RequestMapping("/api/payment")
public class PaymentAPIController {

	@PostMapping(path = "/create")
	public ResponseEntity<?> createPayment(@Validated @RequestParam("amount") int amount,
			@Validated @RequestParam("bankCode") String bankCode,
			@Validated @RequestParam("language") String locate,
			HttpServletRequest req
			) throws IOException {
		
		String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String orderType = "other";
        long total = Integer.parseInt(req.getParameter("amount"))*100;
        
        
        String vnp_TxnRef = Config.getRandomNumber(8);
        String vnp_IpAddr = Config.getIpAddress(req);

        String vnp_TmnCode = Config.vnp_TmnCode;
        
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(total));
        vnp_Params.put("vnp_CurrCode", "VND");
        
        if (bankCode != null && !bankCode.isEmpty()) {
            vnp_Params.put("vnp_BankCode", bankCode);
        }
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
        vnp_Params.put("vnp_OrderType", orderType);


        if (locate != null && !locate.isEmpty()) {
            vnp_Params.put("vnp_Locale", locate);
        } else {
            vnp_Params.put("vnp_Locale", "vn");
        }
        vnp_Params.put("vnp_ReturnUrl", Config.vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        
        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);
        
        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = Config.hmacSHA512(Config.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = Config.vnp_PayUrl + "?" + queryUrl;                   
        return new ResponseEntity<Response>(new Response(true, "success", paymentUrl), HttpStatus.OK);
    }
	@GetMapping("/return")
	public ResponseEntity<?> transaction(
			@Validated @RequestParam("vnp_Amount") long amount,
			@Validated @RequestParam("vnp_BankCode") String bankCode,
			@Validated @RequestParam("vnp_CardType") String cardType,
			@Validated @RequestParam("vnp_OrderInfo") String orderInfo,
			@Validated @RequestParam("vnp_PayDate") String payDate,
			@Validated @RequestParam("vnp_ResponseCode") String responseCode,
			@Validated @RequestParam("vnp_TmnCode") String tmnCode,
			@Validated @RequestParam("vnp_TransactionNo") String transactionNo,
			@Validated @RequestParam("vnp_TransactionStatus") String transactionStatus,
			@Validated @RequestParam("vnp_TxnRef") String txnRef,
			@Validated @RequestParam("vnp_SecureHash") String secureHash			
			){

		if (responseCode.equals("00")) {
			return new ResponseEntity<Response>(new Response(true, "Successfully", ""), HttpStatus.OK);
		}
		else {
			return new ResponseEntity<Response>(new Response(true, "Failed", ""), HttpStatus.OK);
		}
		
	}
		
		
	
}
