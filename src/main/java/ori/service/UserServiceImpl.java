package ori.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import ori.entity.User;
import ori.repository.UserRepository;
@Service
public class UserServiceImpl implements IUserService{
	@Autowired
	UserRepository userRepository;

	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public <S extends User> S save(S entity) {
		return userRepository.save(entity);
	}

	@Override
	public Page<User> findAll(Pageable pageable) {
		return userRepository.findAll(pageable);
	}

	@Override
	public List<User> findAll() {
		return userRepository.findAll();
	}

	@Override
	public Optional<User> findById(Integer id) {
		return userRepository.findById(id);
	}

	@Override
	public long count() {
		return userRepository.count();
	}

	@Override
	public void deleteById(Integer id) {
		userRepository.deleteById(id);
	}

	@Override
	public Optional<User> findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public User updateUser(User model) {
		Integer userid = model.getUserId();
		User user = userRepository.findById(userid).get();
		model.setPasswordHash(user.getPasswordHash());
//		model.setIsAdmin(user.getIsAdmin());
		return userRepository.save(model);
	}
	@Override
	public User updateAddress(String email,String newAddress) {
		
		User user = userRepository.findByEmail(email).get();
		user.setAddress(newAddress);
		return userRepository.save(user);
	}
	@Override
	public Page<User> getAll(Integer pageNo) {
		Pageable pageable = PageRequest.of(pageNo-1, 100);
		return userRepository.findAll(pageable);
	}
	
	public User login(String email, String passwd) {
		User user = userRepository.findByEmail(email).get();
		if (user != null && passwd.equals(user.getPasswordHash())) {
			return user;
		}
		return null;
	}



	@Override
	public Optional<User> getByUserNameOrEmail(String username) {
		return userRepository.findByUsernameOrEmail(username);
	}


}
