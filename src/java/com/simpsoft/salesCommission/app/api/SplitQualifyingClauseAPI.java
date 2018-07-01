package com.simpsoft.salesCommission.app.api;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

import com.simpsoft.salesCommission.app.model.SplitQualifyingClause;




public class SplitQualifyingClauseAPI {
	@Autowired
	private static SessionFactory sessionFactory;
	
	
	public void setSessionFactory(SessionFactory factory) {
		sessionFactory = factory;
	}
	
	
	public List<SplitQualifyingClause> getSplitQualClause(long ruleID) {
		List<SplitQualifyingClause> splitQualClauseList = new ArrayList<SplitQualifyingClause>();
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			//newSplitQualClause = (SplitQualifyingClause) session.get(SplitQualifyingClause.class, ruleID);
			Criteria criteria = session.createCriteria(SplitQualifyingClause.class)
					.add(Restrictions.eq("SPLIT_RUL_ID", ruleID));
			splitQualClauseList=criteria.list();
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return splitQualClauseList;
	}
}
