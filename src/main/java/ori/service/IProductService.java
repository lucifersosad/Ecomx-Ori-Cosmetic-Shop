package ori.service;

import java.util.List;
import java.util.Optional;
import ori.entity.Product;

public interface IProductService {

	void delete(Product entity);
	
	void deleteById(Integer id);

	long count();

	Optional<Product> findById(Integer id);

	List<Product> findAllById(Iterable<Integer> ids);

	List<Product> findAll();

	<S extends Product> S save(S entity);
	
	List<Product> findTop10();
	
	List<Product> findProductsMostSaleByCategory();
	
    List<Product> findByBrand(Integer brandId);
    
    List<Product> findByCategory(Integer cateId);

}
