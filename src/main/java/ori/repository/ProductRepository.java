package ori.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ori.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
	
	@Query(value = "SELECT *" + 
			"FROM ( SELECT *, ROW_NUMBER() OVER (PARTITION BY cateId ORDER BY sale DESC) AS RowNum FROM product) AS RankedProducts\r\n" +
			"WHERE RowNum = 1\r\n" + 
			"ORDER BY sale DESC\r\n" +
			"LIMIT 8", nativeQuery = true)
    List<Product> findProductsMostSaleByCategory();
}
