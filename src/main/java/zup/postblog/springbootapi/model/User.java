package zup.postblog.springbootapi.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.br.CPF;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    @Column(name = "username")
    @NotBlank
    private String username;
    
    @NotBlank
    @Email
    @Column(name = "email", unique = true)
    private String email;
    
    @NotBlank
    @CPF
    @Column(name = "cpf", unique = true)
    private String cpf;
    
    @Column(name = "birthday", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthday;
 
    public User() {
    	super();
    }
 
    public User(String username, String email, String cpf, Date birthday) {
    	 super();
         this.username = username;
         this.email = email;
         this.cpf = cpf;
         this.birthday = birthday;
    }

	public long getUserId() {
		return id;
	}
	public void setUserId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public String getCpf() {
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

//    @Override
//    public String toString() {
//        return "Employee [id=" + id + ", firstName=" + username + ", emailId=" + email + "]";
//    }
 
}