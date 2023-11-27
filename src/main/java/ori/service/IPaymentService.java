package ori.service;

import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ori.entity.Payment;

public interface IPaymentService {

	long count();

	<S extends Payment> Page<S> findAll(Example<S> example, Pageable pageable);

	List<Payment> findAllById(Iterable<Integer> ids);

	List<Payment> findAll();

	<S extends Payment> S save(S entity);

}
