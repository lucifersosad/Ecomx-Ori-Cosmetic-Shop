package ori.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ori.entity.Cart;
import ori.entity.CartKey;



@Repository
public interface CartRepository extends JpaRepository<Cart, CartKey>{
	@Query(value="select * from cart where userid=?",nativeQuery = true)
	List<Cart> findByUserId(Integer userId);
	@Query(value="select * from cart where userid=? and proid=?",nativeQuery = true)
	List<Cart> findByUserIdAndProid(Integer userId,Integer proId);
}
