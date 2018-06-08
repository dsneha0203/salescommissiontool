package com.simpsoft.salesCommission.app.dataloader;

import java.util.Date;
import java.util.List;

public class OrderXML {
	
	private long id;

	private Date orderDate;
	
	private String salesRepresentative;

	private String supportEngineer;
	
	private String administrator;
	
	private String customer;
	
	private  long orderTotal;
	
	private List<OrderLineItemsXML> orderLineItemsXML;
	
	public OrderXML() {
		
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the orderDate
	 */
	public Date getOrderDate() {
		return orderDate;
	}

	/**
	 * @param orderDate the orderDate to set
	 */
	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	/**
	 * @return the salesRepresentative
	 */
	public String getSalesRepresentative() {
		return salesRepresentative;
	}

	/**
	 * @param salesRepresentative the salesRepresentative to set
	 */
	public void setSalesRepresentative(String salesRepresentative) {
		this.salesRepresentative = salesRepresentative;
	}

	/**
	 * @return the supportEngineer
	 */
	public String getSupportEngineer() {
		return supportEngineer;
	}

	/**
	 * @param supportEngineer the supportEngineer to set
	 */
	public void setSupportEngineer(String supportEngineer) {
		this.supportEngineer = supportEngineer;
	}

	/**
	 * @return the administrator
	 */
	public String getAdministrator() {
		return administrator;
	}

	/**
	 * @param administrator the administrator to set
	 */
	public void setAdministrator(String administrator) {
		this.administrator = administrator;
	}

	/**
	 * @return the customer
	 */
	public String getCustomer() {
		return customer;
	}

	/**
	 * @param customer the customer to set
	 */
	public void setCustomer(String customer) {
		this.customer = customer;
	}

	/**
	 * @return the orderTotal
	 */
	public long getOrderTotal() {
		return orderTotal;
	}

	/**
	 * @param orderTotal the orderTotal to set
	 */
	public void setOrderTotal(long orderTotal) {
		this.orderTotal = orderTotal;
	}

	/**
	 * @return the orderLineItemsXML
	 */
	public List<OrderLineItemsXML> getOrderLineItemsXML() {
		return orderLineItemsXML;
	}

	/**
	 * @param orderLineItemsXML the orderLineItemsXML to set
	 */
	public void setOrderLineItemsXML(List<OrderLineItemsXML> orderLineItemsXML) {
		this.orderLineItemsXML = orderLineItemsXML;
	}

}
