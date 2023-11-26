package ori.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ori.entity.Product;
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
	
	@Override
	public List<Product> findProductsMostSaleByCategory() {
		return productRepository.findProductsMostSaleByCategory();
	}
}