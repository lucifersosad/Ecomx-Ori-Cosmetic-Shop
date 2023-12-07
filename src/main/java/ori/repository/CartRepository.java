package ori.repository;


import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ori.entity.Cart;
import ori.entity.CartKey;



@Repository
public interface CartRepository extends JpaRepository<Cart, CartKey>{
	@Query("SELECT c FROM Cart c WHERE c.user.id = :userId")
	List<Cart> findByUserId(@Param("userId") Integer userId);
}
