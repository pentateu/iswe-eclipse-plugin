package nz.co.iswe.samples.jpa;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * Entity implementation class for Entity: ClientEntity
 *
 */
@Entity
public class ClientEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	private Long clientId;
	
	@Column(nullable=false)
	private String name;
	
	private Integer age;
	
	@OneToMany(mappedBy="client", targetEntity=AddressEntity.class)
	private List<AddressEntity> listOfAddress;

	public ClientEntity() {
		super();
	}   
	
	public Long getClientId() {
		return this.clientId;
	}
	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}
	
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Integer getAge() {
		return this.age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	
	
	public List<AddressEntity> getListOfAddress() {
		return listOfAddress;
	}
	public void setListOfAddress(List<AddressEntity> listOfAddress) {
		this.listOfAddress = listOfAddress;
	}
}
