package zup.postblog.springbootapi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "addresses")
public class Address {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
	
	@NotBlank
	@Column(name = "logradouro")
    private String logradouro;
	
	@NotBlank
	@Column(name = "numero")
    private String numero;
	
	@NotBlank
	@Column(name = "complemento")
    private String complemento;
	
	@NotBlank
	@Column(name = "bairro")
    private String bairro;
	
	@NotBlank
	@Column(name = "cidade")
    private String cidade;
	
	@NotBlank
	@Column(name = "estado")
    private String estado;
	
	@NotBlank
	@Column(name = "cep")
    private String cep;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
 
    public Address() {
  
    }
 
    public Address(String logradouro, String numero, String complemento, String bairro, String cidade, String estado, String cep) {
         this.logradouro = logradouro;
         this.numero = numero;
         this.complemento = complemento;
         this.bairro = bairro;
         this.cidade = cidade;
         this.cidade = estado;
         this.cidade = cep;
    }

    public long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public String getLogradouro() {
		return logradouro;
	}
	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getComplemento() {
		return complemento;
	}
	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getBairro() {
		return bairro;
	}
	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCidade() {
		return cidade;
	}
	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getCep() {
		return cep;
	}
	public void setCep(String cep) {
		this.cep = cep;
	}

	public void setUser(User user) {
		this.user = user;
	}

//	@Override
//    public String toString() {
//        return "Address [id=" + id + ", firstName=" + username + ", emailId=" + email + "]";
//    }
 
}