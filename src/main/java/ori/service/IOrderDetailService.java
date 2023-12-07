package ori.service;

import java.util.List;
import java.util.Optional;

import ori.entity.OrderDetail;
import ori.entity.OrderDetailKey;

public interface IOrderDetailService {

	void deleteById(OrderDetailKey id);

	long count();

	Optional<OrderDetail> findById(OrderDetailKey id);

	List<OrderDetail> findAll();

	<S extends OrderDetail> S save(S entity);

}
