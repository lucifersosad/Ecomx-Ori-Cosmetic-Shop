package ori.service;

import java.util.List;
import java.util.Optional;

import ori.entity.Category;

public interface ICategoryService {

	void deleteById(Integer id);

	long count();

	Optional<Category> findById(Integer id);

	List<Category> findAllById(Iterable<Integer> ids);

	List<Category> findAll();

	<S extends Category> S save(S entity);

}
