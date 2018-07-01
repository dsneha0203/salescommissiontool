package com.simpsoft.salesCommission.app.api;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.simpsoft.salesCommission.app.model.Employee;
import com.simpsoft.salesCommission.app.model.OrderDetail;
import com.simpsoft.salesCommission.app.model.OrderLineItems;
import com.simpsoft.salesCommission.app.model.OrderLineItemsSplit;
import com.simpsoft.salesCommission.app.model.OrderRoster;
import com.simpsoft.salesCommission.app.model.Rule;
import com.simpsoft.salesCommission.app.model.SplitQualifyingClause;
import com.simpsoft.salesCommission.app.model.SplitRule;

@Component
public class SplitRuleAPI {

	@Autowired
	private static SessionFactory sessionFactory;
	
	@Autowired
	private OrderAPI orderApi;

	private static final Logger logger = Logger.getLogger(SplitRuleAPI.class);

	public void setSessionFactory(SessionFactory factory) {
		sessionFactory = factory;
	}
	
	/**
	 * Method to create split rule
	 * @param splitRule
	 * @return
	 */
	public SplitRule createSplitRule(SplitRule splitRule) {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		SplitRule newSplitRule = new SplitRule();
		try {
			tx = session.beginTransaction();
			newSplitRule.setSplitRuleName(splitRule.getSplitRuleName());
			newSplitRule.setDescription(splitRule.getDescription());
			newSplitRule.setStartDate(splitRule.getStartDate());
			newSplitRule.setEndDate(splitRule.getEndDate());
			newSplitRule.setSplitQualifyingClause(splitRule.getSplitQualifyingClause());
			newSplitRule.setSplitRuleBeneficiary(splitRule.getSplitRuleBeneficiary());
			session.save(newSplitRule);
			tx.commit();
			logger.debug("CREATED AN SPLIT RULE INTO DATABASE" + newSplitRule);
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return newSplitRule;
	}
	
	/**
	 * Method for getting list of split rules
	 * @return
	 */
	public List<SplitRule> listOfSplitRule() {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		List<SplitRule> splitRules = new ArrayList<SplitRule>();		
		try {
			tx = session.beginTransaction();
			splitRules = session.createQuery("FROM SplitRule").list();
			for (Iterator iterator = splitRules.iterator(); iterator.hasNext();) {
				SplitRule splitRule = (SplitRule) iterator.next();				
			}
			tx.commit();
			logger.debug("GOT LIST OF SPLIT RULES FROM DATABASE");
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return splitRules;
	}

/**
 * Method for Assign split rules to qualified order line items
 * @param orderRoster
 */
	public void assignSplitRule(OrderRoster orderRoster) {
	
		List<SplitRule> splitRulesList = listOfSplitRule();
		List<SplitQualifyingClause> newSpltQList = new ArrayList<SplitQualifyingClause>();
		List<OrderDetail> orderDetailsList = orderRoster.getOrderDetail();
		
		SplitRule splitRules = new SplitRule();
		List<SplitQualifyingClause> spltQClauseLst = new ArrayList<SplitQualifyingClause>();
		
		for (Iterator iterator = orderDetailsList.iterator(); iterator.hasNext();) 
		{
			
			OrderDetail orderDetail = (OrderDetail) iterator.next();
			List<OrderLineItems> orderLineItemsList = orderDetail.getOrderLineItems();
			
			for (Iterator iterator1 = orderLineItemsList.iterator(); iterator1.hasNext();) 
			{
				OrderLineItems orderLineItems = (OrderLineItems) iterator1.next();
				
				for (Iterator iterator2 = splitRulesList.iterator(); iterator2.hasNext();) 
				{
					splitRules = (SplitRule) iterator2.next();
					spltQClauseLst = splitRules.getSplitQualifyingClause();
					
					for (Iterator iterator3 = spltQClauseLst.iterator(); iterator3.hasNext();) 
					{
						SplitQualifyingClause spltQClause = (SplitQualifyingClause) iterator3.next();
						
						if(spltQClause.getFieldList().getDisplayName().equals("Duty Percentage") && spltQClause.isNotFlag()== false && spltQClause.getConditionList().getConditionValue().equals("equal") )
						{		
							if((Integer.parseInt(spltQClause.getValue())) == (orderLineItems.getDutyPercentage())){
								newSpltQList.add(spltQClause);
							}						
						}
						else if (spltQClause.getFieldList().getDisplayName().equals("Duty Percentage") && spltQClause.isNotFlag()== true && spltQClause.getConditionList().getConditionValue().equals("equal") )
						{
							if((Integer.parseInt(spltQClause.getValue())) != (orderLineItems.getDutyPercentage())){
								newSpltQList.add(spltQClause);
							}
						}
						else if (spltQClause.getFieldList().getDisplayName().equals("Duty Percentage") && spltQClause.isNotFlag()== false && spltQClause.getConditionList().getConditionValue().equals("less than") )
						{
							if((Integer.parseInt(spltQClause.getValue())) > (orderLineItems.getDutyPercentage())){
								newSpltQList.add(spltQClause);
							}
						}
						else if (spltQClause.getFieldList().getDisplayName().equals("Duty Percentage") && spltQClause.isNotFlag()== true && spltQClause.getConditionList().getConditionValue().equals("less than") )
						{
							if((Integer.parseInt(spltQClause.getValue())) < (orderLineItems.getDutyPercentage())){
								newSpltQList.add(spltQClause);
							}
						}
						else if (spltQClause.getFieldList().getDisplayName().equals("Duty Percentage") && spltQClause.isNotFlag()== false && spltQClause.getConditionList().getConditionValue().equals("greater than") )
						{
							if((Integer.parseInt(spltQClause.getValue())) < (orderLineItems.getDutyPercentage())){
								newSpltQList.add(spltQClause);
							}
						}
						else if (spltQClause.getFieldList().getDisplayName().equals("Duty Percentage") && spltQClause.isNotFlag()== true && spltQClause.getConditionList().getConditionValue().equals("greater than") )
						{
							if((Integer.parseInt(spltQClause.getValue())) > (orderLineItems.getDutyPercentage())){
								newSpltQList.add(spltQClause);
							}
						}
						else if (spltQClause.getFieldList().getDisplayName().equals("Order Total") && spltQClause.isNotFlag()== false && spltQClause.getConditionList().getConditionValue().equals("equal") )
						{
							if((Integer.parseInt(spltQClause.getValue())) == (orderLineItems.getSubtotal())){
								newSpltQList.add(spltQClause);
							}
						}
						else if (spltQClause.getFieldList().getDisplayName().equals("Order Total") && spltQClause.isNotFlag()== true && spltQClause.getConditionList().getConditionValue().equals("equal") )
						{
							if((Integer.parseInt(spltQClause.getValue())) != (orderLineItems.getSubtotal())){
								newSpltQList.add(spltQClause);
							}
						}
						else if (spltQClause.getFieldList().getDisplayName().equals("Order Total") && spltQClause.isNotFlag()== false && spltQClause.getConditionList().getConditionValue().equals("less than") )
						{
							if((Integer.parseInt(spltQClause.getValue())) > (orderLineItems.getSubtotal())){
								newSpltQList.add(spltQClause);
							}
						}
						else if (spltQClause.getFieldList().getDisplayName().equals("Order Total") && spltQClause.isNotFlag()== true && spltQClause.getConditionList().getConditionValue().equals("less than") )
						{
							if((Integer.parseInt(spltQClause.getValue())) < (orderLineItems.getSubtotal())){
								newSpltQList.add(spltQClause);
							}
						}
						else if (spltQClause.getFieldList().getDisplayName().equals("Order Total") && spltQClause.isNotFlag()== false && spltQClause.getConditionList().getConditionValue().equals("greater than") )
						{
							if((Integer.parseInt(spltQClause.getValue())) < (orderLineItems.getSubtotal())){
								newSpltQList.add(spltQClause);
							}
						}
						else if (spltQClause.getFieldList().getDisplayName().equals("Order Total") && spltQClause.isNotFlag()== true && spltQClause.getConditionList().getConditionValue().equals("greater than") )
						{
							if((Integer.parseInt(spltQClause.getValue())) > (orderLineItems.getSubtotal())){
								newSpltQList.add(spltQClause);
							}
						}
						else if (spltQClause.getFieldList().getDisplayName().equals("Discount Percentage") && spltQClause.isNotFlag()== false && spltQClause.getConditionList().getConditionValue().equals("equal") )
						{
							if((Integer.parseInt(spltQClause.getValue())) == (orderLineItems.getDiscountPercentage())){
								newSpltQList.add(spltQClause);
							}
						}
						else if (spltQClause.getFieldList().getDisplayName().equals("Discount Percentage") && spltQClause.isNotFlag()== true && spltQClause.getConditionList().getConditionValue().equals("equal") )
						{
							if((Integer.parseInt(spltQClause.getValue())) != (orderLineItems.getDiscountPercentage())){
								newSpltQList.add(spltQClause);
							}
						}
						else if (spltQClause.getFieldList().getDisplayName().equals("Discount Percentage") && spltQClause.isNotFlag()== false && spltQClause.getConditionList().getConditionValue().equals("less than") )
						{
							if((Integer.parseInt(spltQClause.getValue())) > (orderLineItems.getDiscountPercentage())){
								newSpltQList.add(spltQClause);
							}
						}
						else if (spltQClause.getFieldList().getDisplayName().equals("Discount Percentage") && spltQClause.isNotFlag()== true && spltQClause.getConditionList().getConditionValue().equals("less than") )
						{
							if((Integer.parseInt(spltQClause.getValue())) < (orderLineItems.getDiscountPercentage())){
								newSpltQList.add(spltQClause);
							}
						}
						else if (spltQClause.getFieldList().getDisplayName().equals("Discount Percentage") && spltQClause.isNotFlag()== false && spltQClause.getConditionList().getConditionValue().equals("greater than") )
						{
							if((Integer.parseInt(spltQClause.getValue())) < (orderLineItems.getDiscountPercentage())){
								newSpltQList.add(spltQClause);
							}
						}
						else if (spltQClause.getFieldList().getDisplayName().equals("Discount Percentage") && spltQClause.isNotFlag()== true && spltQClause.getConditionList().getConditionValue().equals("greater than") )
						{
							if((Integer.parseInt(spltQClause.getValue())) > (orderLineItems.getDiscountPercentage())){
								newSpltQList.add(spltQClause);
							}
						}
						else if (spltQClause.getFieldList().getDisplayName().equals("Customer Name") && spltQClause.isNotFlag()== false && spltQClause.getConditionList().getConditionValue().equals("equal") )
						{
							if((spltQClause.getValue()).equals(orderDetail.getCustomer().getCustomerName())){
								newSpltQList.add(spltQClause);
							}
						}
					}				
					if(newSpltQList.size() == spltQClauseLst.size())
					{			
						setSplitRuleToOrderLineItem(splitRules,orderLineItems);
						newSpltQList.clear();
						break;
					}
					else
					{
						newSpltQList.clear();
					}
				}
				
				
			}
		}
		
		updateStatusOfOrderRoster(orderRoster);
		
		logger.debug("ASSIGNED SPLIT RULE TO QUALIFIED ORDER LINE ITEM AND CHANGED STATUS OF ORDER ROSTER TO VERIFIED");
	}

/**
 * Method for database operation of split rule assignment	
 * @param splitRule
 * @param orderLineItems
 */
	public void setSplitRuleToOrderLineItem(SplitRule splitRule, OrderLineItems orderLineItems) {
		
		Session session = sessionFactory.openSession();
		Transaction tx = null;	
		try {
			tx = session.beginTransaction();
			OrderLineItemsSplit orderLineItemsSplit = new OrderLineItemsSplit();
			orderLineItemsSplit.setSplitRule(splitRule);
		//	orderLineItems.setOrderLineItemsSplit(orderLineItemsSplit);
			session.update(orderLineItems);
			tx.commit();
			logger.debug("SET SPLIT RULES TO ORDER LINEITEMS");
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		
	}

/**
 * 	Update status of OrderRoster
 * @param orderRoster
 */
	public void updateStatusOfOrderRoster(OrderRoster orderRoster) {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			orderRoster.setStatus("verified");
			session.update(orderRoster);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}
	
	
	public SplitRule getSplitRule(long ruleID) {
		
		SplitRule newSplitRule = new SplitRule();
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			newSplitRule = (SplitRule) session.get(SplitRule.class, ruleID);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return newSplitRule;
	}
	
	
	public void editSplitRule(SplitRule splitRule) {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			SplitRule splitRule1 = (SplitRule) session.get(SplitRule.class, splitRule.getId());
			splitRule1.setId(splitRule.getId());
			splitRule1.setSplitRuleName(splitRule.getSplitRuleName());
			splitRule1.setDescription(splitRule.getDescription());
			splitRule1.setStartDate(splitRule.getStartDate());
			splitRule1.setEndDate(splitRule.getEndDate());
			session.merge(splitRule1);
			logger.debug("EDIT THE SPLIT RULE DETAILS FROM DATABASE" + splitRule1);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}

	}
	
	
}
