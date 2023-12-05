package ori.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;

@Entity(name = "shopping_session")
public class Shopping_Session {
	@EmbeddedId
    private ShoppingSessionKey id;
	
	@ManyToOne
    @MapsId("proId")
    @JoinColumn(name = "proid")
    Product product;
	
	@ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "userid")
    User user;
	
	String date;
}
