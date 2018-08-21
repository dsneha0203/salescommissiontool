package com.simpsoft.salesCommission.app.calculation;

import java.util.List;
import java.util.Scanner;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.simpsoft.salesCommission.app.api.EmployeeAPI;
import com.simpsoft.salesCommission.app.api.OrderAPI;
import com.simpsoft.salesCommission.app.api.RuleAPI;
import com.simpsoft.salesCommission.app.api.RuleAssignmentAPI;
import com.simpsoft.salesCommission.app.api.RuleSimpleAPI;
import com.simpsoft.salesCommission.app.model.Employee;
import com.simpsoft.salesCommission.app.model.OrderLineItemsSplit;
import com.simpsoft.salesCommission.app.model.QualifyingClause;
import com.simpsoft.salesCommission.app.model.Rule;
import com.simpsoft.salesCommission.app.model.RuleAssignment;
import com.simpsoft.salesCommission.app.model.RuleAssignmentDetails;
import com.simpsoft.salesCommission.app.model.RuleComposite;
import com.simpsoft.salesCommission.app.model.RuleSimple;

public class CalculateCompAmountSimpleInd {
	
	private static final Logger logger = Logger.getLogger(CalculateCompAmountSimpleInd.class);
	private static Date startDate=null;
	private static Date endDate=null;
	public static void main(String[] args) throws ParseException {
		
		ApplicationContext context = 
	            new ClassPathXmlApplicationContext("/applicationContext.xml");
		EmployeeAPI empAPI = (EmployeeAPI) context.getBean("employeeApi");
		RuleAssignmentAPI ruleAssAPI =(RuleAssignmentAPI) context.getBean("ruleAssignmentApi");
		RuleSimpleAPI ruleSimpAPI =(RuleSimpleAPI) context.getBean("ruleSimpleApi");
		RuleAPI ruleAPI =(RuleAPI) context.getBean("ruleApi");
		OrderAPI orderAPI =(OrderAPI) context.getBean("orderApi");
		
		
		
		//save start and end dates in calc roster table
//		System.out.println("Enter start date: ");
//		String sDate1= new Scanner(System.in).next();
		String sDate1 = args[0];
		logger.debug("sDATE= "+sDate1);
		startDate=new SimpleDateFormat("dd/MM/yyyy").parse(sDate1);
		logger.debug("START DATE= "+startDate);
//		System.out.println("Enter end date: ");
//		String sDate2= new Scanner(System.in).next();		
		String sDate2 = args[1];
		logger.debug("eDATE= "+sDate2);
		 endDate=new SimpleDateFormat("dd/MM/yyyy").parse(sDate2);
		logger.debug("END DATE= "+endDate);
		ruleAssAPI.saveDates(startDate, endDate);
		
		
		//find rule assignments for all employee
		findRuleAssgForAllEmp(empAPI, ruleAssAPI, ruleAPI, orderAPI);
		
		
		
		
		
	}

	/**
	 * @param empAPI
	 * @param ruleAssAPI
	 * @param ruleAPI
	 * @param orderAPI
	 */
	private static void findRuleAssgForAllEmp(EmployeeAPI empAPI, RuleAssignmentAPI ruleAssAPI, RuleAPI ruleAPI,
			OrderAPI orderAPI) {
		logger.debug("---FINDING RULE ASSIGNMENTS---");
		List<Employee> empList = empAPI.listEmployees();
		for(Employee emp: empList) {
			int counter=0;
			RuleAssignment assignment= ruleAssAPI.searchAssignedRuleForEmployee(emp.getEmployeeName());
			
			if(assignment != null) {
				
				logger.debug("RuleAssignment id = "+ assignment.getId());
				
					List<RuleAssignmentDetails> assignmentDetails = assignment.getRuleAssignmentDetails();
					for(RuleAssignmentDetails details : assignmentDetails) {
						// check whether the start and end dates of the rule assignment detail falls between
						// the start and end date input values
						boolean valid= checkValidity(ruleAssAPI, details.getId(), startDate, endDate);
						logger.debug("Validity= "+valid);
						if(valid) {
							logger.debug("RuleAssignment detail id = "+ details.getId());
							logger.debug("RULE ID= "+ details.getRule().getId());
							logger.debug("RULE NAME= "+ details.getRule().getRuleName());						
							//check if the rule is composite
							if(details.getRule().getRuleType().equals("Composite")) {
								logger.debug("RULE TYPE = COMPOSITE");
								RuleComposite composite = ruleAPI.findCompRule(details.getRule().getId());
								logger.debug("COMPOSITE RULE ID= "+ composite.getId());
								List<Rule> simpleRules = ruleAssAPI.getSimpleRuleList(composite.getId());
								for(Rule simpRule : simpleRules) {								
									RuleSimple ruleSimple = ruleAPI.findSimpleRule(simpRule.getId());
									logger.debug("SIMPLE RULE NAME= "+ruleAPI.getRule(simpRule.getId()).getRuleName());
									logger.debug("SIMPLE RULE ID= "+ruleSimple.getId());
									List<QualifyingClause> clauses = ruleSimple.getQualifyingClause();
									if(clauses != null) {
										logger.debug("---QUALIFYING CLAUSE LIST---");
										printQualClause(clauses);
									}
									
								}
							}else {
								logger.debug("RULE TYPE = SIMPLE");
								RuleSimple ruleSimple = ruleAPI.findSimpleRule(details.getRule().getId());
								List<QualifyingClause> clauses = ruleSimple.getQualifyingClause();
								if(clauses != null) {
									logger.debug("---QUALIFYING CLAUSE LIST---");
									printQualClause(clauses);
								}
							}
							counter=counter+1;
						}
						
					}
					if(counter>0) {
	//				find order line item split list for the employee
						logger.debug("---FINDING ORDER LINE SPLIT DATA FOR THE EMPLOYEE---");
						List<OrderLineItemsSplit> empSplitList = orderAPI.getLineItemSplitListForEmp(emp.getId());
						if(empSplitList != null) {
							logger.debug("---LINE ITEM SPLIT LIST FOR EMP ID ="+emp.getId()+"---");
							for(OrderLineItemsSplit itemsSplit : empSplitList) {
								logger.debug("LINE ITEM SPLIT ID = "+ itemsSplit.getId());
							}
						}
				}
					
			}
		}
	}
	private static boolean checkValidity(RuleAssignmentAPI ruleAssAPI, long ruleAssDetailId, Date startDate2, Date endDate2) {
		RuleAssignmentDetails assignmentDetails = ruleAssAPI.getRuleAssignmentDetail(ruleAssDetailId);
		Date ruleAssDetStartDate = assignmentDetails.getStartDate();
		Date ruleAssDetEndDate = assignmentDetails.getEndDate();
		if(ruleAssDetStartDate.before(startDate)) {
			return false;
		}
		else if(ruleAssDetEndDate.after(endDate)) {
			return false;
		}
		
		return true;
	}
	
	
	
	private static void printQualClause(List<QualifyingClause> clauses) {
		for(QualifyingClause clause : clauses) {
			logger.debug("QUAL CLAUSE ID= "+clause.getId());
			logger.debug("Field name= "+clause.getFieldList().getDisplayName());
			logger.debug("Condition value= "+clause.getConditionList().getConditionValue());
			if(clause.getAggregateFunctions() != null) {
				logger.debug("Agg Func= "+clause.getAggregateFunctions().getFunctionName());
			}
			logger.debug("Not= "+clause.isNotFlag());
			logger.debug("Value= "+clause.getValue());
		}
	}
	
}
