package ori.service;

import java.util.List;
import ori.entity.Cart;

public interface ICartService {

	void deleteAll();

	void delete(Cart entity);

	long count();

	List<Cart> findAll();

	<S extends Cart> S save(S entity);
	
	List<Cart> findByUserId( Integer userId);
	


	


}
