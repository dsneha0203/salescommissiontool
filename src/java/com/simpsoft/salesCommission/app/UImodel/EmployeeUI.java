package com.simpsoft.salesCommission.app.UImodel;

public class EmployeeUI {
	
	private String employeeName;
	private String currentManager;
	
	public String getCurrentManager() {
		return currentManager;
	}

	public void setCurrentManager(String currentManager) {
		this.currentManager = currentManager;
	}

	

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	
	public EmployeeUI(){		
	}
	
	public EmployeeUI(String employeeName){
		this.employeeName=employeeName;
		
		
	}

}
