package ori.service;

import java.util.List;
import java.util.Optional;

import ori.entity.Category;

public interface ICategoryService {

	void deleteById(Long id);

	long count();

	Optional<Category> findById(Long id);

	List<Category> findAllById(Iterable<Long> ids);

	List<Category> findAll();

	<S extends Category> S save(S entity);

}
