package ori.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ori.entity.Category;
import ori.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
	
	@Query(value = "SELECT *" + 
			"FROM ( SELECT *, ROW_NUMBER() OVER (PARTITION BY cateId ORDER BY sale DESC) AS RowNum FROM product) AS RankedProducts\r\n" +
			"WHERE RowNum = 1\r\n" + 
			"ORDER BY sale DESC\r\n" +
			"LIMIT 8", nativeQuery = true)
    List<Product> findProductsMostSaleByCategory();
	@Query(value = "SELECT * FROM product where brandId = :id ORDER BY ABS(price - :proPrice) LIMIT 4 ", nativeQuery = true)
	List<Product> findByBrand(@Param("id") Integer branId, @Param("proPrice") float proPrice);
	
	@Query(value = "SELECT * FROM product where cateId = :id ORDER BY ABS(price - :proPrice) LIMIT 4 ", nativeQuery = true)
	List<Product> findByCategory(@Param("id") Integer cateId, @Param("proPrice") float proPrice);
	Page<Product> findByCategory(Category category, Pageable pageable);
	List<Product> findByCategory(Category category);
	@Query("SELECT p FROM Product p WHERE p.price >= :startPrice AND p.price <= :endPrice")
    List<Product> findProductsByPriceRange(@Param("startPrice") float startPrice, @Param("endPrice") float endPrice);
}
