package ori.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ori.entity.ShoppingSession;

@Repository
public interface ShoppingSessionRepository extends JpaRepository<ShoppingSession, Integer>{

	@Query(value = "SELECT * FROM shopping_session where  userId = :id limit 3", nativeQuery = true)
	List<ShoppingSession> findByUser(@Param("id") Integer userId);
}
