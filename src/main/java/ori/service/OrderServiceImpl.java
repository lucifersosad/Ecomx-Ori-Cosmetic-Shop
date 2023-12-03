package ori.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ori.entity.Order;
import ori.model.OrderModel;
import ori.repository.OrderRepository;


@Service
public class OrderServiceImpl implements IOrderService {
	@Autowired
	OrderRepository orderRepository;

	public OrderServiceImpl(OrderRepository orderRepository) {
		super();
		this.orderRepository = orderRepository;
	}

	@Override
	public int reOnCurrentMonth() {

		return orderRepository.revenueOnCurrentMonth();
	}

	@Override
	public int reOnCurrentYear() {
		// TODO Auto-generated method stub
		return orderRepository.revenueOnCurrentYear();
	}

	@Override
	public float rateCom() {
		// TODO Auto-generated method stub
		return orderRepository.rateCompleted();
	}

	@Override
	public int reOnCurrentQuarter() {
		// TODO Auto-generated method stub
		return orderRepository.revenueOnCurrentQuarter();

	}

	@Override
	public List<Order> findAll() {
		// TODO Auto-generated method stub
		return orderRepository.findAll();
	}

	@Override
	public Optional<Order> findById(Integer id) {
		// TODO Auto-generated method stub
		return orderRepository.findById(id);
	}

	@Override
	public <S extends Order> S save(S entity) {
		// TODO Auto-generated method stub
		return orderRepository.save(entity);
	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub
		orderRepository.deleteById(id);
	}



}
