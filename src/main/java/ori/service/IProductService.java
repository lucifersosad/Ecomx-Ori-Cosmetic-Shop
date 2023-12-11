package ori.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import ori.entity.Product;
import ori.entity.User;

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
	
    List<Product> findByBrand(Integer brandId, float proPrice);
    
    List<Product> findByCategory(Integer cateId, float proPrice);
    
    Page<Product> getAll(Integer pageNo);

}
