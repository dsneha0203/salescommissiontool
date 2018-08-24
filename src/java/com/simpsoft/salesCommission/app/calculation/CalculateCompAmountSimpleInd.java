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
import com.simpsoft.salesCommission.app.api.SplitRuleAPI;
import com.simpsoft.salesCommission.app.model.Employee;
import com.simpsoft.salesCommission.app.model.OrderDetail;
import com.simpsoft.salesCommission.app.model.OrderLineItems;
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
		SplitRuleAPI splitRuleAPI = (SplitRuleAPI) context.getBean("splitRuleApi");
		
		
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
		findRuleAssgForAllEmp(empAPI, ruleAssAPI, ruleAPI, orderAPI, splitRuleAPI);
		
		
		
		
		
	}

	/**
	 * @param empAPI
	 * @param ruleAssAPI
	 * @param ruleAPI
	 * @param orderAPI
	 */
	private static void findRuleAssgForAllEmp(EmployeeAPI empAPI, RuleAssignmentAPI ruleAssAPI, RuleAPI ruleAPI,
			OrderAPI orderAPI, SplitRuleAPI splitRuleAPI) {
		logger.debug("---FINDING RULE ASSIGNMENTS---");
		List<Employee> empList = empAPI.listEmployees();
		for(Employee emp: empList) {
			int counter=0;
			RuleAssignment assignment= ruleAssAPI.searchAssignedRuleForEmployee(emp.getId());
			
			if(assignment != null) {
				List<Rule> rulesAssigned = new ArrayList<>();
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
							rulesAssigned.add(details.getRule());

							counter=counter+1;
						}
						
					}
					if(counter>0) {
	//				find order line item split list for the employee
						logger.debug("---FINDING ORDER LINE SPLIT DATA FOR THE EMPLOYEE---");
						List<OrderLineItemsSplit> empSplitList = orderAPI.getLineItemSplitListForEmp(emp.getId());
						//list of all line item details for this emp
						List<OrderLineItems> empLineItemsList = new ArrayList<>();
						if(empSplitList != null) {
							logger.debug("---LINE ITEM SPLIT LIST FOR EMP ID ="+emp.getId()+"---");
							for(OrderLineItemsSplit itemsSplit : empSplitList) {
								logger.debug("LINE ITEM SPLIT ID = "+ itemsSplit.getId());
								
								// get line item details for this line item split
								OrderLineItems items= splitRuleAPI.getOrderLineItem(itemsSplit.getId());
								empLineItemsList.add(items);
							}
						}
						
						// compare qual clause list of each rule with each item of empLineItemsList
						for(OrderLineItems lineItems : empLineItemsList) {
							boolean addLineItem=compareQualList(orderAPI, ruleAssAPI, ruleAPI, lineItems,rulesAssigned );
						}
				}
					
			}
		}
	}

	/**
	 * @param ruleAPI
	 * @param details
	 */
	private static List<QualifyingClause> getSimpRuleDetails(RuleAPI ruleAPI, Rule simpRule) {
		logger.debug("RULE TYPE = SIMPLE");
		RuleSimple ruleSimple = ruleAPI.findSimpleRule(simpRule.getId());
		List<QualifyingClause> clauses = ruleSimple.getQualifyingClause();
		if(clauses != null) {
			logger.debug("---QUALIFYING CLAUSE LIST---");
			printQualClause(clauses);
		}
		return clauses;
	}

	/**
	 * @param ruleAssAPI
	 * @param ruleAPI
	 * @param details
	 */
	private static List<Rule> getCompRuleDetails(RuleAssignmentAPI ruleAssAPI, RuleAPI ruleAPI,
			Rule compRule) {
		logger.debug("RULE TYPE = COMPOSITE");
		RuleComposite composite = ruleAPI.findCompRule(compRule.getId());
		logger.debug("COMPOSITE RULE ID= "+ composite.getId());
		List<Rule> simpleRules = ruleAssAPI.getSimpleRuleList(composite.getId());
//		for(Rule simpRule : simpleRules) {								
//			RuleSimple ruleSimple = ruleAPI.findSimpleRule(simpRule.getId());
//			logger.debug("SIMPLE RULE NAME= "+ruleAPI.getRule(simpRule.getId()).getRuleName());
//			logger.debug("SIMPLE RULE ID= "+ruleSimple.getId());
//			List<QualifyingClause> clauses = ruleSimple.getQualifyingClause();
//			if(clauses != null) {
//				logger.debug("---QUALIFYING CLAUSE LIST---");
//				printQualClause(clauses);
//			}
//			
//		}
		return simpleRules;
	}
	private static boolean compareQualList(OrderAPI orderAPI,RuleAssignmentAPI ruleAssAPI,RuleAPI ruleAPI, 
			OrderLineItems lineItems, List<Rule> rulesAssigned) {
		
		for(Rule rule: rulesAssigned) {
			if(rule.getRuleType().equals("Composite")) {
				List<Rule> simpleRules= getCompRuleDetails(ruleAssAPI, ruleAPI, rule);
				for(Rule simpRule : simpleRules) {
					List<QualifyingClause> qualList = getSimpRuleDetails(ruleAPI, simpRule);
				}
			}else {
				List<QualifyingClause> qualList = getSimpRuleDetails(ruleAPI, rule);
				for(QualifyingClause clause : qualList) {
					boolean notFlag = clause.isNotFlag();
					String condition = clause.getConditionList().getConditionValue();
					if(clause.getAggregateFunctions() == null) {
						boolean satisfy= check(orderAPI, clause.getFieldList().getDisplayName(), notFlag, condition, clause.getValue(),lineItems.getId() );
					}
					
				}
			}
		}
		return false;
	}

	private static boolean check(OrderAPI orderAPI,String displayName, boolean notFlag, String condition, String value, long lineItemId) {
		OrderLineItems items = orderAPI.getOrderLineItem(lineItemId);
		// get order detail record of this order line item
		OrderDetail orderDetail = orderAPI.getOrderDetailFromLineItem(lineItemId);
		switch(displayName) {
		case "Discount Percentage":
			if(condition.equals("equal")) {
				if(items.getDiscountPercentage() == Integer.parseInt(value) ){
					if(!notFlag) {
						return true;
					}
					
				}
			}else if(condition.equals("less than")) {
				if(items.getDiscountPercentage() < Integer.parseInt(value)) {
					if(!notFlag) {
						return true;
					}
					
				}
			}else if(condition.equals("greater than")) {
				if(items.getDiscountPercentage() > Integer.parseInt(value)) {
					if(!notFlag) {
						return true;
					}
				}
			}else if(condition.equals("less than equal to")) {
				if(items.getDiscountPercentage() <= Integer.parseInt(value)) {
					if(!notFlag) {
						return true;
					}
					
				}
			}else if(condition.equals("greater than equal to")) {
				if(items.getDiscountPercentage() >= Integer.parseInt(value)) {
					if(!notFlag) {
						return true;
					}
				}
			}
			break;
		case "Duty Percentage":
			if(condition.equals("equal")) {
				if(items.getDutyPercentage() == Integer.parseInt(value) ){
					if(!notFlag) {
						return true;
					}
					
				}
			}else if(condition.equals("less than")) {
				if(items.getDutyPercentage() < Integer.parseInt(value)) {
					if(!notFlag) {
						return true;
					}
					
				}
			}else if(condition.equals("greater than")) {
				if(items.getDutyPercentage() > Integer.parseInt(value)) {
					if(!notFlag) {
						return true;
					}
				}
			}else if(condition.equals("less than equal to")) {
				if(items.getDutyPercentage() <= Integer.parseInt(value)) {
					if(!notFlag) {
						return true;
					}
					
				}
			}else if(condition.equals("greater than equal to")) {
				if(items.getDutyPercentage() >= Integer.parseInt(value)) {
					if(!notFlag) {
						return true;
					}
				}
			}
			break;
		case "Customer Name":
			String custName =orderDetail.getCustomer().getCustomerName();
			
			if(condition.equals("equal")) {
				if(custName.equalsIgnoreCase(value)) {
					if(!notFlag) {
						return true;
					}
				}
			}else if(condition.equals("starts with")) {
				if(custName.startsWith(value)) {
					
				}
			}
			break;
		case "Office Location":
			break;
		case "Product Type":
			break;
		case "Quantity":
			break;
		case "Product Name":
			break;
		case "Sale Type":
			break;
		case "Order Total":
			break;
		}
		
		return false;
		
	}

	private static boolean checkValidity(RuleAssignmentAPI ruleAssAPI, long ruleAssDetailId, Date startDate2, Date endDate2) {
		RuleAssignmentDetails assignmentDetails = ruleAssAPI.getRuleAssignmentDetail(ruleAssDetailId);
		String validityType = assignmentDetails.getValidityType();
		Date ruleAssDetStartDate = assignmentDetails.getStartDate();
		Date ruleAssDetEndDate = assignmentDetails.getEndDate();
		
		if(validityType.equals("repeats")) {
			if(  (ruleAssDetStartDate.equals(startDate)) || (ruleAssDetStartDate.after(startDate))  ) {
				return true;
			}
			else if( (ruleAssDetEndDate.equals(endDate)) || (ruleAssDetEndDate.before(endDate)) ) {
				return true;
			}
		}else {
			if(  (ruleAssDetStartDate.equals(startDate)) || (ruleAssDetStartDate.after(startDate))  ) {
				if( (ruleAssDetEndDate.equals(endDate)) || (ruleAssDetEndDate.before(endDate)) ) {
					return true;
				}
			}
		}
		

		return false;
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
