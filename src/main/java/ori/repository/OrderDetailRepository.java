package ori.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ori.entity.OrderDetail;
import ori.entity.OrderDetailKey;


@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, OrderDetailKey>{

}
