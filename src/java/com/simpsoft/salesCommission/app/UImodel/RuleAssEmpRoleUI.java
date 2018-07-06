package com.simpsoft.salesCommission.app.UImodel;

public class RuleAssEmpRoleUI {
	private String empName;
	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
	}
	private String roleName;
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
	
	RuleAssEmpRoleUI(){
		
	}

RuleAssEmpRoleUI(String empName, String roleName){
	this.empName=empName;
	this.roleName=roleName;
		
	}
}
