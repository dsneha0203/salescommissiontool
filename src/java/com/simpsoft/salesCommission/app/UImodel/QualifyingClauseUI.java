package com.simpsoft.salesCommission.app.UImodel;

public class QualifyingClauseUI {

	private String value;
	private String conditionValue;
	private String fieldName;
	private boolean condition;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getConditionValue() {
		return conditionValue;
	}

	public void setConditionValue(String conditionValue) {
		this.conditionValue = conditionValue;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public QualifyingClauseUI(){
		
	}
	
public QualifyingClauseUI(String value, String conditionValue, String fieldName, boolean condition){
	
	this.value = value;
	this.fieldName = fieldName;
	this.conditionValue = conditionValue;
	this.condition = condition;
		
	}

public boolean getCondition() {
	return condition;
}

public void setCondition(boolean condition) {
	this.condition = condition;
}


}
