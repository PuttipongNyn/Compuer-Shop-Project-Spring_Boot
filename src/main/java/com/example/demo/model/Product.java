package com.example.demo.model;

import java.util.Arrays;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import javax.persistence.Lob;

import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "product")
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, unique = true)
	private Long id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "description", nullable = false)
	private String description;

	@Column(name = "price", nullable = false, precision = 10, scale = 2)
	private double price;

	@Column(name = "quantity", nullable = false, precision = 10, scale = 2)
	private int quantity;

	@Lob
	@Column(name = "Image", length = Integer.MAX_VALUE, nullable = true)
	private byte[] image;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date", nullable = false)
    private Date createDate;
    
    @Column(name = "category", nullable = false)
	private String category;

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Product() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Override
//	public String toString() {
//		return "Product [id=" + id + ", name=" + name + ", description=" + description + ", price=" + price + ", image="
//				+ Arrays.toString(image) + "]";
//	}
	public String toString() {
		return "Product [id=" + id + ", name=" + name + ", description=" + description + ", category" + category +", price=" + price + ", image="
				+ Arrays.toString(image) + ", createDate=" + createDate + "]";
	}

}