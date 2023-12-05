package ori.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;

@Entity(name = "shopping_session")
public class ShoppingSession {
	@EmbeddedId
    private ShoppingSessionKey id;
	
	@ManyToOne
    @MapsId("proId")
    @JoinColumn(name = "proid")
    private Product product;
	
	@ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "userid")
    private User user;
	
	String date;

	public ShoppingSessionKey getId() {
		return id;
	}

	public void setId(ShoppingSessionKey id) {
		this.id = id;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
}
