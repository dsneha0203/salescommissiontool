package com.simpsoft.salesCommission.app.calculation;

import java.util.List;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.simpsoft.salesCommission.app.api.EmployeeAPI;
import com.simpsoft.salesCommission.app.api.RuleAssignmentAPI;
import com.simpsoft.salesCommission.app.model.Employee;
import com.simpsoft.salesCommission.app.model.RuleAssignment;
import com.simpsoft.salesCommission.app.model.RuleAssignmentDetails;

public class CalculateCompAmountSimpleInd {
	
	private static final Logger logger = Logger.getLogger(CalculateCompAmountSimpleInd.class);
	public static void main(String[] args) throws ParseException {
		
		ApplicationContext context = 
	            new ClassPathXmlApplicationContext("/applicationContext.xml");
		EmployeeAPI empAPI = (EmployeeAPI) context.getBean("employeeApi");
		RuleAssignmentAPI ruleAssAPI =(RuleAssignmentAPI) context.getBean("ruleAssApi");
		
		String sDate1 = args[0];
		Date startDate=new SimpleDateFormat("dd/MM/yyyy").parse(sDate1);
		String sDate2 = args[1];
		Date endDate=new SimpleDateFormat("dd/MM/yyyy").parse(sDate2);

		
		List<RuleAssignment> ruleAsgList = ruleAssAPI.listRuleAssignment();
		for(Iterator ruleAsgItr= ruleAsgList.iterator(); ruleAsgItr.hasNext();) {
			RuleAssignment ruleAssg = (RuleAssignment)ruleAsgItr.next();
			if(ruleAssg.getEmployee() != null) {
				long empId = ruleAssg.getEmployee().getId();
				long ruleAssgId = ruleAssg.getId();
				//get rule assignment details list of that particular rule assignment
				List<RuleAssignmentDetails> ruleAsgDetailList = ruleAssg.getRuleAssignmentDetails();	
				for(Iterator ruleAsgDetailItr= ruleAsgDetailList.iterator(); ruleAsgDetailItr.hasNext();) {
					RuleAssignmentDetails ruleAsgDetail = (RuleAssignmentDetails)ruleAsgDetailItr.next();
					
				}
				
			}
			
			
		}
		
	}
	
}
