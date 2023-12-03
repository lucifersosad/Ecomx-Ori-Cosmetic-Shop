package ori.repository;




import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ori.entity.Order;
import ori.model.OrderModel;



@Repository
public interface OrderRepository extends JpaRepository<Order, Integer>{

	@Query(value = "SELECT IFNULL(sum(order_total), 0) as total_month\r\n"
			+ "FROM `order` \r\n"
			+ "WHERE MONTH(order_date) = MONTH(CURRENT_DATE()) AND order_status=1;", nativeQuery = true)
    int revenueOnCurrentMonth();
	@Query(value = "SELECT IFNULL(sum(order_total), 0) as total_month\r\n"
			+ "FROM `order` \r\n"
			+ "WHERE YEAR(order_date) = YEAR(CURRENT_DATE()) AND order_status=1;", nativeQuery = true)
    int revenueOnCurrentYear();
	@Query(value = "SELECT IFNULL(sum(order_status)/count(*),0)*100 as completed_rate\r\n"
			+ "FROM `order` \r\n", nativeQuery = true)
    int rateCompleted();
	@Query(value = "SELECT IFNULL(sum(order_total), 0) as total_quarter\r\n"
			+ "FROM `order` \r\n"
			+ "WHERE quarter(order_date) = quarter(CURRENT_DATE())\r\n"
			+ "AND order_status=1;", nativeQuery = true)
    int revenueOnCurrentQuarter();
	

}
