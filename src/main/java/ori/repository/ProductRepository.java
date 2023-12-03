package ori.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
	@Query(value = "SELECT * FROM product where brandId = :id ORDER BY ABS(price - :proPrice) LIMIT 3 ", nativeQuery = true)
	List<Product> findByBrand(@Param("id") Integer branId, @Param("proPrice") float proPrice);
	
	@Query(value = "SELECT * FROM product where cateId = :id ORDER BY ABS(price - :proPrice) LIMIT 3 ", nativeQuery = true)
	List<Product> findByCategory(@Param("id") Integer cateId, @Param("proPrice") float proPrice);
}
