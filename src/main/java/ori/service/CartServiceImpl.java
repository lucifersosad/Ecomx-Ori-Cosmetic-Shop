package ori.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ori.entity.Cart;
import ori.repository.CartRepository;

@Service
public class CartServiceImpl implements ICartService{
	@Autowired
	CartRepository cartRepository;
	


	@Override
	public <S extends Cart> S save(S entity) {
		return cartRepository.save(entity);
	}

	@Override
	public List<Cart> findAll() {
		return cartRepository.findAll();
	}



	@Override
	public long count() {
		return cartRepository.count();
	}

	@Override
	public void delete(Cart entity) {
		cartRepository.delete(entity);
	}

	@Override
	public void deleteAll() {
		cartRepository.deleteAll();
	}

	@Override
	public List<Cart> findByUserId(Integer userId) {

		return cartRepository.findByUserId(userId);
	}





}
