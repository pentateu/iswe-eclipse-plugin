package nz.co.iswe.samples.jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Entity implementation class for Entity: AddressEntity
 *
 */
@Entity
public class AddressEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long addressId;
	private String streetName;
	private String cityName;
	private ClientEntity client;
	

	public AddressEntity() {
		super();
	}   
	
	@Id
	public Long getAddressId() {
		return this.addressId;
	}
	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}
	
	@Column(nullable=false, length=200)
	public String getStreetName() {
		return this.streetName;
	}
	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}
	
	@Column(nullable=false, length=200)
	public String getCityName() {
		return this.cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	
	@ManyToOne(targetEntity=ClientEntity.class)
	public ClientEntity getClient() {
		return this.client;
	}
	public void setClient(ClientEntity client) {
		this.client = client;
	}
   
}
