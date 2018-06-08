package com.simpsoft.salesCommission.app.UImodel;

import java.util.Date;
import java.util.List;

import com.simpsoft.salesCommission.app.model.RuleAssignmentParameter;

public class RuleAssUI {
	private int id;
	private String ruleName;
	private String fixed;
	private String repeats;
	private String frequency;
	public String getFrequency() {
		return frequency;
	}
	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	private Date Startdate;
	private Date endDate;
	//private String overwrite;
	//public String getOverwrite() {
		//return overwrite;
	//}
	//public void setOverwrite(String overwrite) {
	//	this.overwrite = overwrite;
	//}
	public String getFixed() {
		return fixed;
	}
	public void setFixed(String fixed) {
		this.fixed = fixed;
	}
	public String getRepeats() {		
		return repeats;
	}
	public void setRepeats(String repeats) {
		this.repeats = repeats;
	}
	
	private List<RuleAssignmentParameter> ruleAssignmentParameter ;
	
	
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	/*public String getValidityType() {
		return ValidityType;
	}
	public void setValidityType(String validityType) {
		ValidityType = validityType;
	}*/
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getRuleName() {
		return ruleName;
	}
	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}
	public Date getStartdate() {
		return Startdate;
	}
	public void setStartdate(Date Startdate) {
		this.Startdate = Startdate;
	}
	
	
	public List<RuleAssignmentParameter> getRuleAssignmentParameter() {
		return ruleAssignmentParameter;
	}
	public void setRuleAssignmentParameter(List<RuleAssignmentParameter> ruleAssignmentParameter) {
		this.ruleAssignmentParameter = ruleAssignmentParameter;
	}
	
	public RuleAssUI(){		
	}
	public RuleAssUI(int id,String ruleName,String fixed,String repeats,Date Startdate,Date endDate,String overwrite, String frequency){
		this.id=id;
		this.ruleName=ruleName;
		this.fixed=fixed;
		this.repeats=repeats;
		//this.ValidityType=ValidityType;
		this.Startdate=Startdate;
		this.endDate=endDate;
	//	this.overwrite=overwrite;
		this.frequency=frequency;
		
	}
	
}
