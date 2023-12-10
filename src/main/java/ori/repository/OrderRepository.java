package ori.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ori.entity.Order;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ori.model.OrderModel;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer>{
	@Query(value = "SELECT IFNULL(sum(total), 0) as total_month FROM `orders`  \r\n"
			+ "WHERE MONTH(STR_TO_DATE(date, '%d/%m/%Y %H:%i:%s')) = MONTH(CURRENT_DATE()) \r\n"
			+ "AND status=1 ;", nativeQuery = true)
    int revenueOnCurrentMonth();
	@Query(value = "SELECT IFNULL(sum(total), 0) as total_month FROM `orders`  \r\n"
			+ "WHERE YEAR(STR_TO_DATE(date, '%d/%m/%Y %H:%i:%s')) = YEAR(CURRENT_DATE()) \r\n"
			+ "AND status=1 ;", nativeQuery = true)
    int revenueOnCurrentYear();
	@Query(value = "SELECT IFNULL(sum(status)/count(*),0)*100 as completed_rate\r\n"
			+ "FROM `orders` \r\n", nativeQuery = true)
    int rateCompleted();
	@Query(value = "SELECT IFNULL(sum(total), 0) as total_month FROM `orders`  \r\n"
			+ "WHERE QUARTER(STR_TO_DATE(date, '%d/%m/%Y %H:%i:%s')) = QUARTER(CURRENT_DATE()) \r\n"
			+ "AND status=1 ;", nativeQuery = true)
    int revenueOnCurrentQuarter();
	@Query("""
		    SELECT o FROM Order o WHERE o.userId.userId = :userId
		""")
		List<Order> findOrderByUserId(@Param("userId") Integer userId);
}