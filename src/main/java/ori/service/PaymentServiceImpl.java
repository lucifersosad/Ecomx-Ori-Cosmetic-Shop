package ori.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import ori.entity.Payment;
import ori.repository.PaymentRepository;

@Service
public class PaymentServiceImpl  implements IPaymentService{
	@Autowired
	PaymentRepository paymentRepository;

	public PaymentServiceImpl(PaymentRepository paymentRepository) {
		super();
		this.paymentRepository = paymentRepository;
	}

	@Override
	public List<Payment> findAll() {
		return paymentRepository.findAll();
	}

	@Override
	public List<Payment> findAllById(Iterable<Integer> ids) {
		return paymentRepository.findAllById(ids);
	}

	@Override
	public <S extends Payment> Page<S> findAll(Example<S> example, Pageable pageable) {
		return paymentRepository.findAll(example, pageable);
	}

	@Override
	public long count() {
		return paymentRepository.count();
	}
	
}
