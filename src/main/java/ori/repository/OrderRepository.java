package ori.repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ori.entity.Order;



@Repository
public interface OrderRepository extends JpaRepository<Order, Integer>{
	@Query("""
		    SELECT o FROM Order o WHERE o.userId.userId = :userId
		""")
		List<Order> findOrderByUserId(@Param("userId") Integer userId);

}