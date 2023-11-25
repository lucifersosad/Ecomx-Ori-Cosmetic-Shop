package ori.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ori.entity.User;

public interface IUserService {

	void deleteById(Integer id);

	long count();

	Optional<User> findById(Integer id);

	List<User> findAll();

	Page<User> findAll(Pageable pageable);

	<S extends User> S save(S entity);
	User updateUser(User model);
}
