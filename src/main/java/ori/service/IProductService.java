package ori.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ori.entity.Category;
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

	Page<Product> findAll(Pageable pageable);
	
	public Page<Product> findByCategory(Category category, Pageable pageable);

	List<Product> findByCategory(Category category);

	List<Product> findProductsByPriceRange(float start_price, float end_price);
}
