package assgn2;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;


public class Moderator {
   
	private String id;
	
	@NotEmpty	
	private String name;
	
	@NotEmpty
	private String email;
	
	@NotEmpty
	@Length(min = 6, max = 15, message="Password is not Valid. Select password with minimum 2 charaters to maximum 14")
	private String password;
	private String created_at;
	

	public Moderator(){}
	
	
	public Moderator(String id, String name, String email, String password, String created_at) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.created_at= created_at;
	}
	
	
	
	public String getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getEmail() {
		return email;
	}
	public String getPassword() {
		return password;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getcreated_at() {
		return created_at;
	}

	public void setcreated_at(String created_at) {
		this.created_at = created_at;
	}

}
