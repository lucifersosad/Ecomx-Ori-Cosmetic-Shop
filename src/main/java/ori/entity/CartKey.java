package ori.entity;

import java.util.Objects;

import jakarta.persistence.Embeddable;

@Embeddable
public class CartKey {
	private Integer userid;
	private Integer proid;
	public CartKey(Integer userid, Integer proid) {
	
		this.userid = userid;
		this.proid = proid;
	}
	public CartKey() {

	}
	public Integer getUserid() {
		return userid;
	}
	public void setUserid(Integer userid) {
		this.userid = userid;
	}
	public Integer getProid() {
		return proid;
	}
	public void setPorid(Integer proid) {
		this.proid = proid;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartKey ratingKey = (CartKey) o;
        return Objects.equals(userid, ratingKey.userid) &&
               Objects.equals(proid, ratingKey.proid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userid, proid);
    }
}


