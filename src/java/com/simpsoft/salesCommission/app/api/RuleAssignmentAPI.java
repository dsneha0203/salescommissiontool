package com.simpsoft.salesCommission.app.api;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.simpsoft.salesCommission.app.dataloader.RuleCompositeXML;
import com.simpsoft.salesCommission.app.model.Employee;
import com.simpsoft.salesCommission.app.model.Frequency;
import com.simpsoft.salesCommission.app.model.Role;
import com.simpsoft.salesCommission.app.model.Rule;
import com.simpsoft.salesCommission.app.model.RuleAssignment;
import com.simpsoft.salesCommission.app.model.RuleAssignmentDetails;
import com.simpsoft.salesCommission.app.model.RuleAssignmentParameter;
import com.simpsoft.salesCommission.app.model.RuleParameter;

@Component
public class RuleAssignmentAPI {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private RuleAPI ruleAPI;

	private static final Logger logger = Logger.getLogger(RuleAssignmentAPI.class);

	public void setSessionFactory(SessionFactory factory) {
		sessionFactory = factory;
	}

	/**
	 * Method for Create assignment of rule to role or employee
	 * 
	 * @param ruleAss
	 * @return
	 */

	public long createRuleAssignment(RuleAssignment ruleAss) {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		RuleAssignment newRuleAss = new RuleAssignment();
		try {
			tx = session.beginTransaction();
			Employee emp =  ruleAss.getEmployee();
			if(emp != null){
			newRuleAss.setEmployee(ruleAss.getEmployee());
			}else{
			newRuleAss.setRole(ruleAss.getRole());
			}
			newRuleAss.setRuleAssignmentDetails(ruleAss.getRuleAssignmentDetails());
			session.save(newRuleAss);
			tx.commit();
			logger.debug("CREATED AN RULE ASSIGNMENT INTO DATABASE" + newRuleAss);
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return newRuleAss.getId();
	}

	/**
	 * Method for getting one rule assignment based on ID
	 * 
	 * @param ruleID
	 * @return
	 */
	public RuleAssignment getRuleAssignment(long ruleAssID) {
		RuleAssignment newRuleAss = new RuleAssignment();

		Session session = sessionFactory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			newRuleAss = (RuleAssignment) session.get(RuleAssignment.class, ruleAssID);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return newRuleAss;
	}
	
	/**
	 * Method for editing rule assignment
	 * @param ruleAss
	 */
	public void editRuleAssignment(RuleAssignment ruleAss) {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			RuleAssignment newRuleAss = (RuleAssignment) session.get(RuleAssignment.class, ruleAss.getId());
			Employee emp = ruleAss.getEmployee();
			if(emp!=null)
			{
				newRuleAss.setEmployee(ruleAss.getEmployee());
			}
			else
			{
				newRuleAss.setRole(ruleAss.getRole());
			}
			
			List<RuleAssignmentDetails>  newRuleAssignmentDetailsList = new ArrayList<RuleAssignmentDetails>();
			List<RuleAssignmentDetails>  ruleAssDtl = ruleAss.getRuleAssignmentDetails();
			for (Iterator iterator = ruleAssDtl.iterator(); iterator.hasNext();) {

				RuleAssignmentDetails ruleAssignmentDetails = (RuleAssignmentDetails) iterator.next();
				RuleAssignmentDetails newRuleAssignmentDetails = new RuleAssignmentDetails();
				newRuleAssignmentDetails.setValidityType(ruleAssignmentDetails.getValidityType());
				newRuleAssignmentDetails.setFrequency(ruleAssignmentDetails.getFrequency());
				newRuleAssignmentDetails.setStartDate(ruleAssignmentDetails.getStartDate());
				newRuleAssignmentDetails.setEndDate(ruleAssignmentDetails.getEndDate());
				newRuleAssignmentDetails.setRuleAssignmentParameter(ruleAssignmentDetails.getRuleAssignmentParameter());
				newRuleAssignmentDetails.setRule(ruleAssignmentDetails.getRule());
				newRuleAssignmentDetailsList.add(newRuleAssignmentDetails);
			}
		//	List<RuleAssignmentDetails> newRuleAssignmentDetails = editRuleAssignmentDetails(session,ruleAss.getRuleAssignmentDetails());
			newRuleAss.setRuleAssignmentDetails(newRuleAssignmentDetailsList);
			session.merge(newRuleAss);
			tx.commit();
			logger.debug("EDITED AN RULE ASSIGNMENT INTO DATABASE" + newRuleAss);
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		
	}
	
	public List<RuleAssignmentDetails> editRuleAssignmentDetails(Session session,List<RuleAssignmentDetails> ruleAssDtl) {
		
		List<RuleAssignmentDetails>  newRuleAssignmentDetailsList = new ArrayList<RuleAssignmentDetails>();
	
			for (Iterator iterator = ruleAssDtl.iterator(); iterator.hasNext();) {

				RuleAssignmentDetails ruleAssignmentDetails = (RuleAssignmentDetails) iterator.next();
				RuleAssignmentDetails newRuleAssignmentDetails = new RuleAssignmentDetails();
				newRuleAssignmentDetails.setValidityType(ruleAssignmentDetails.getValidityType());
				newRuleAssignmentDetails.setFrequency(ruleAssignmentDetails.getFrequency());
				newRuleAssignmentDetails.setStartDate(ruleAssignmentDetails.getStartDate());
				newRuleAssignmentDetails.setEndDate(ruleAssignmentDetails.getEndDate());
				newRuleAssignmentDetails.setRuleAssignmentParameter(ruleAssignmentDetails.getRuleAssignmentParameter());
				newRuleAssignmentDetails.setRule(ruleAssignmentDetails.getRule());
				newRuleAssignmentDetailsList.add(newRuleAssignmentDetails);
			}
			session.merge(newRuleAssignmentDetailsList);
		
			logger.debug("EDITED AN RULE ASSIGNMENT INTO DATABASE" + newRuleAssignmentDetailsList);
	
		return newRuleAssignmentDetailsList;
	}

	/**
	 * Method for create Frequency
	 * 
	 * @param conditionList
	 * @return
	 */
	public long createFrequency(Frequency frequency) {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		Frequency freq = new Frequency();
		try {
			tx = session.beginTransaction();
			freq.setFrequencyName(frequency.getFrequencyName());
			session.save(freq);
			tx.commit();
			logger.debug("CREATED AN FREQUENCY INTO DATABASE" + freq);
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return freq.getId();
	}

	/**
	 * Method for searching frequency by name
	 * 
	 * @param freqName
	 * @return
	 */
	public Frequency searchFrequency(String freqName) {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		Frequency freq = new Frequency();
		try {
			tx = session.beginTransaction();
			List frequencies = session.createQuery("FROM Frequency").list();
			for (Iterator iterator = frequencies.iterator(); iterator.hasNext();) {

				Frequency frequency = (Frequency) iterator.next();
				if (freqName.equals(frequency.getFrequencyName())) {
					freq = frequency;
				}
			}
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return freq;
	}
	
	public List<Frequency> listOfFrequency() {

		Session session = sessionFactory.openSession();
		Transaction tx = null;
		tx = session.beginTransaction();
		List frequencyList = session.createQuery("FROM Frequency").list();
		for (Iterator iterator = frequencyList.iterator(); iterator.hasNext();) {
			Frequency frequency = (Frequency) iterator.next();
			logger.debug("GET THE RULE DETAILS FROM DATABASE= " + frequency.getId() +" "+ frequency.getFrequencyName());

		}
		return frequencyList;
	}
	/**
	 * Method to set base parameters of chosen rule to assignment rule details
	 * table parameters
	 * 
	 * @param rule
	 * @return
	 */
	public List<RuleAssignmentParameter> setRuleAssignmentParameters(Rule rule) {

		List<RuleAssignmentParameter> assParamList = new ArrayList<RuleAssignmentParameter>();
		List<RuleParameter> rparamlist = rule.getRuleParameter();
		for (Iterator iterator = rparamlist.iterator(); iterator.hasNext();) {
			RuleParameter rparam = (RuleParameter) iterator.next();
			String paramName = rparam.getParameterName();
			String paramValue = rparam.getParameterValue();
			RuleAssignmentParameter assParam = new RuleAssignmentParameter();
			assParam.setParameterName(paramName);
			assParam.setOverwriteValue(paramValue);
			assParamList.add(assParam);
		}
		return assParamList;
	}

	private RuleAssignment searchAssignedRuleForRole(String queryName) {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		RuleAssignment ruleAssignment = new RuleAssignment();
		try {
			tx = session.beginTransaction();
			Criteria crit = session.createCriteria(RuleAssignment.class);
			crit.add(Restrictions.isNotNull("role"));
			List assignmentList = crit.list();
			for (Iterator iterator = assignmentList.iterator(); iterator.hasNext();) {

				RuleAssignment assignment = (RuleAssignment) iterator.next();
				if (queryName.equals(assignment.getRole().getRoleName())) {
					ruleAssignment = assignment;
					break;
				}
			}
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return ruleAssignment;
	}

	private RuleAssignment searchAssignedRuleForEmployee(String queryName) {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		RuleAssignment ruleAssignment = new RuleAssignment();
		try {
			tx = session.beginTransaction();
			Criteria crit = session.createCriteria(RuleAssignment.class);
			crit.add(Restrictions.isNotNull("employee"));
			List assignmentList = crit.list();
			for (Iterator iterator = assignmentList.iterator(); iterator.hasNext();) {

				RuleAssignment assignment = (RuleAssignment) iterator.next();
				if (queryName.equals(assignment.getEmployee().getEmployeeName())) {
					ruleAssignment = assignment;
					break;
				}
			}
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return ruleAssignment;
	}

	public RuleAssignment searchAssignedRule(String queryName) {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		Role role = new Role();
		RuleAssignment ruleAssignment = new RuleAssignment();
		try {
			tx = session.beginTransaction();
			List roleList = session.createQuery("FROM Role").list();
			for (Iterator iterator = roleList.iterator(); iterator.hasNext();) {

				Role role1 = (Role) iterator.next();
				if (queryName.equals(role1.getRoleName())) {
					role = role1;
					ruleAssignment = searchAssignedRuleForRole(queryName);
					break;
				}
			}
			if (role != null) {
				List empList = session.createQuery("FROM Employee").list();
				for (Iterator iterator = empList.iterator(); iterator.hasNext();) {

					Employee emp1 = (Employee) iterator.next();
					if (queryName.equals(emp1.getEmployeeName())) {
						ruleAssignment = searchAssignedRuleForEmployee(queryName);
						break;
					}
				}
			}
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		if(ruleAssignment != null) {
		return ruleAssignment;
		}else {
			return null;
		}
	}
	
	/**
	 * Method for getting rule for assignment
	 * @param ruleID
	 * @return
	 */
	public RuleAssignmentDetails getRuleToAssign(long ruleID){
		
		RuleAssignmentDetails rulAssDetail = new RuleAssignmentDetails();
		Rule rule = ruleAPI.getRule(ruleID);
		rulAssDetail.setRule(rule);
		rulAssDetail.setRuleAssignmentParameter(setRuleAssignmentParameters(rule));   
		return rulAssDetail;
	}
}
