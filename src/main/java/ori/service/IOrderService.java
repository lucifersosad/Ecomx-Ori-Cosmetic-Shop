package ori.service;

import java.util.List;
import java.util.Optional;

import ori.entity.Category;
import ori.entity.Order;


public interface IOrderService {
	 int reOnCurrentMonth();
	 
	 int reOnCurrentYear();
	 
	 int reOnCurrentQuarter();
	 
	 float rateCom();
	 
	 List<Order> findAll();
	 
	Optional<Order> findById(Integer id);
	<S extends Order> S save(S entity);
	void deleteById(Integer id);
}
