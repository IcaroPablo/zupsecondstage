package zup.postblog.springbootapi.model;

import java.util.List;

public class UserAndAddresses {
	private User user;
	private List<Address> addresses;
	
	public UserAndAddresses(User user, List<Address> addresses) {
		super();
		this.user = user;
		this.addresses = addresses;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public List<Address> getAddresses() {
		return addresses;
	}
	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}
	
	
}
