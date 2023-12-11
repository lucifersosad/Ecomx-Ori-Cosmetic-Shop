package ori.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import ori.entity.Category;
import ori.entity.Product;
import ori.entity.User;
import ori.repository.ProductRepository;

@Service
public class ProductServiceImpl implements IProductService  {
	@Autowired
	ProductRepository productRepository;

	@Override
	public <S extends Product> S save(S entity) {
		return productRepository.save(entity);
	}

	@Override
	public List<Product> findAll() {
		return productRepository.findAll();
	}

	@Override
	public List<Product> findAllById(Iterable<Integer> ids) {
		return productRepository.findAllById(ids);
	}

	@Override
	public Optional<Product> findById(Integer id) {
		return productRepository.findById(id);
	}

	@Override
	public long count() {
		return productRepository.count();
	}

	@Override
	public void delete(Product entity) {
		productRepository.delete(entity);
	}
	
	@Override
	public List<Product> findTop10() {
		Pageable pageable = PageRequest.of(0, 10);
		return productRepository.findAll(pageable).toList();
	}
	public void deleteById(Integer id) {
		productRepository.deleteById(id);
		
	}

	@Override
	public List<Product> findProductsMostSaleByCategory() {
		return productRepository.findProductsMostSaleByCategory();
	}

	@Override
	public List<Product> findByBrand(Integer brandId, float proPrice) {
		return productRepository.findByBrand(brandId, proPrice);
	}

	@Override
	public List<Product> findByCategory(Integer cateId, float proPrice) {
		return productRepository.findByCategory(cateId, proPrice);
	}
	
	@Override
	public Page<Product> getAll(Integer pageNo) {
		Pageable pageable = PageRequest.of(pageNo - 1, 10);
		return productRepository.findAll(pageable);
	}
	@Override
	public Page<Product> findAll(Pageable pageable) {
		return productRepository.findAll(pageable);
	}
	@Override
	public Page<Product> findByCategory(Category category, Pageable pageable) {
		return productRepository.findByCategory(category, pageable);
	}
	
	
}
