package com.simpsoft.salesCommission.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import com.simpsoft.salesCommission.app.UImodel.AddRuleUI;
import com.simpsoft.salesCommission.app.UImodel.AssignmentUI;
import com.simpsoft.salesCommission.app.UImodel.EmployeeUI;
import com.simpsoft.salesCommission.app.UImodel.RoleUI;
import com.simpsoft.salesCommission.app.UImodel.RuleAss1UI;
import com.simpsoft.salesCommission.app.UImodel.RuleAssEmpRoleUI;
import com.simpsoft.salesCommission.app.UImodel.RuleAssUI;
import com.simpsoft.salesCommission.app.UImodel.RuleListContainer;
import com.simpsoft.salesCommission.app.UImodel.RuleListContainer1;
import com.simpsoft.salesCommission.app.api.EmployeeAPI;
import com.simpsoft.salesCommission.app.api.RoleAPI;
import com.simpsoft.salesCommission.app.api.RuleAPI;
import com.simpsoft.salesCommission.app.api.RuleAssignmentAPI;
import com.simpsoft.salesCommission.app.model.Employee;
import com.simpsoft.salesCommission.app.model.Frequency;
import com.simpsoft.salesCommission.app.model.Role;
import com.simpsoft.salesCommission.app.model.Rule;
import com.simpsoft.salesCommission.app.model.RuleAssignment;
import com.simpsoft.salesCommission.app.model.RuleAssignmentDetails;
import com.simpsoft.salesCommission.app.model.RuleAssignmentParameter;
import com.simpsoft.salesCommission.app.model.RuleParameter;

@Controller

public class TestCompController {
	@Autowired
	private RoleAPI roleApi;
	@Autowired
	private RuleAPI ruleApi;
	@Autowired
	private EmployeeAPI empApi;
	@Autowired
	private RuleAssignmentAPI ruleAssApi;
	
	
	
	
	
	
	
	@RequestMapping(value = "/compplan", method = RequestMethod.GET)
	public ModelAndView listRules(ModelMap model, HttpServletRequest request, HttpSession session,
			SessionStatus status) {

		model.addAttribute("listRole", roleApi.listOfRoles());
		model.addAttribute("listRules", ruleApi.listOfRules());
		model.addAttribute("listfrequency", ruleAssApi.listOfFrequency());
		model.addAttribute("targetlist", empApi.listOfTargetDefinitions());

		if (session.getAttribute("ruleListContainer") == null)
			session.setAttribute("ruleListContainer", getDummyRuleListContainer());
		model.addAttribute("ruleListContainer", (RuleListContainer) session.getAttribute("ruleListContainer"));

		/*---------------------------Finding compensation assignment for role------------------------------*/

		RoleUI object = (RoleUI) request.getSession().getAttribute("roleName");
		if (object != null) {
			RuleAssignment rAssdtail = ruleAssApi.searchAssignedRule(object.getRoleName());
			if(rAssdtail != null) {
			List<RuleAssignmentDetails> ptr1 = rAssdtail.getRuleAssignmentDetails();
			if(ptr1 != null) {
			ArrayList<RuleAssignmentDetails> rl1 = new ArrayList<RuleAssignmentDetails>();
			ArrayList<Role> rleName = new ArrayList<Role>();
			model.addAttribute("assObj", rAssdtail);
			List<RuleAssignmentParameter> parameterList = new ArrayList<RuleAssignmentParameter>();

			Iterator<RuleAssignmentDetails> it1 = ptr1.iterator();
			while (it1.hasNext()) {
				Role roleasign = roleApi.searchRoleByName(object.getRoleName());
				model.addAttribute("rlelist", roleasign);
				System.out.println("The Value Is" + roleasign);
				RuleAssignmentDetails rp = (RuleAssignmentDetails) it1.next();
				List<RuleAssignmentParameter> ptr2 = rp.getRuleAssignmentParameter();
				System.out.println(rp.getRule().getRuleName());
				System.out.println(rp.getRule().getId());
				rl1.add(rp);
			}
			model.addAttribute("List2", rl1);
			status.setComplete();
			//session.removeAttribute("roleName");
			return new ModelAndView("CompPlan");
					}
				}else {
				return new ModelAndView("CompPlan");
					}
			
			
			}

		/*-----------------------------------Finding compensation assignment for Employee------------------------------------*/
		EmployeeUI object1 = (EmployeeUI) request.getSession().getAttribute("EmployeeName");

		if (object1 != null) {
			RuleAssignment rAssdtail = ruleAssApi.searchAssignedRule(object1.getEmployeeName());
			if(rAssdtail != null) {
			List<RuleAssignmentDetails> ptr1 = rAssdtail.getRuleAssignmentDetails();
			if(ptr1 != null) {
			ArrayList<Employee> empName = new ArrayList<Employee>();
			model.addAttribute("assObj1", rAssdtail);
			ArrayList<RuleAssignmentDetails> rl1 = new ArrayList<RuleAssignmentDetails>();

			Iterator<RuleAssignmentDetails> it1 = ptr1.iterator();
			while (it1.hasNext()) {
				List<Employee> rasd = empApi.searchEmployeesByName(object1.getEmployeeName());
				List<Employee> ptr = empApi.listEmployees();
				Iterator<Employee> it3 = rasd.iterator();
				while (it3.hasNext()) {
					Employee e = (Employee) it3.next();
					System.out.println(e.getEmployeeName());
					empName.add(e);
				}
				RuleAssignmentDetails rp = (RuleAssignmentDetails) it1.next();
				List<RuleAssignmentParameter> ptr2 = rp.getRuleAssignmentParameter();
				System.out.println(rp.getRule().getRuleName());
				System.out.println(rp.getRule().getId());
				rl1.add(rp);
			}
			model.addAttribute("List2", rl1);
			model.addAttribute("empList", empName);
			status.setComplete();
			session.removeAttribute("EmployeeName");
			return new ModelAndView("CompPlan");
			}
			}else {
				return new ModelAndView("CompPlan");
			}
		}
		/*-----------------------------------creating table for compensation plan against ruleId------------------------------------*/
		AddRuleUI object2 = (AddRuleUI) request.getSession().getAttribute("Id");
		if (object2 != null) {
			Rule rul = ruleApi.getRule(object2.getId());
			ArrayList<Rule> rl1 = new ArrayList<Rule>();
			List<RuleParameter> ptr2 = rul.getRuleParameter();
			Iterator it = ptr2.iterator();
			while (it.hasNext()) {
				RuleParameter r = (RuleParameter) it.next();
				System.out.println(r.getParameterName());
				System.out.println(r.getParameterValue());
			}
			rl1.add(rul);
			model.addAttribute("ruleList", rl1);
			status.setComplete();
			session.removeAttribute("Id");
			return new ModelAndView("CompPlan");
			
		}
		return new ModelAndView("CompPlan");
		
	}

	/*-----------------------------------Dummy loop for RulelistContainer------------------------------------*/

	private RuleListContainer getDummyRuleListContainer() {
		List<RuleAssUI> ruleList = new ArrayList<RuleAssUI>();
		for (int i = 0; i < 1; i++) {
			ruleList.add(new RuleAssUI());
		}
		return new RuleListContainer(ruleList);
	}

	/*-----------------------------------------------------Updating Table----------------------------------------------------*/
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(@ModelAttribute("ruleListContainer") RuleListContainer ruleListContainer, AssignmentUI assnmnt,
			ModelMap model) {

		model.addAttribute("id", assnmnt.getId());
		System.out.println("chkId: " + assnmnt.getId());
		model.addAttribute("assRoleName", assnmnt.getName());
		System.out.println("name: " + assnmnt.getName());
		model.addAttribute("assid", assnmnt.getAssid());
		System.out.println("chkId: " + assnmnt.getAssid());
		model.addAttribute("assempName", assnmnt.getEmpName());
		System.out.println("name: " + assnmnt.getEmpName());

		for (RuleAssUI rul1 : ruleListContainer.getRuleList()) {
			System.out.println("Id:   " + rul1.getId());
			System.out.println("RuleName:  " + rul1.getRuleName());
			System.out.println("Fixed:  " + rul1.getFixed());
			System.out.println("Repeats:  " + rul1.getRepeats());
			System.out.println("Frequency:  " + rul1.getFrequency());
			// System.out.println("Validity Type: " + rul1.getValidityType());
			System.out.println("StartDate:  " + rul1.getStartdate());
			System.out.println("EndDate:  " + rul1.getEndDate());
			for (RuleAssignmentParameter rul2 : rul1.getRuleAssignmentParameter()) {
				System.out.println("ParameterName: " + rul2.getParameterName());
				System.out.println("OverwriteValue: " + rul2.getOverwriteValue());
			}
		}

		List<RuleAssUI> rul = ruleListContainer.getRuleList();
		List<RuleAssignmentDetails> rp = new ArrayList<>();
		for (Iterator iterator = rul.iterator(); iterator.hasNext();) {
			RuleAssUI rpm = (RuleAssUI) iterator.next();
			Rule rule = ruleApi.searchRuleByName(rpm.getRuleName());
			RuleAssignmentDetails rass = new RuleAssignmentDetails();
			rass.setRule(rule);
			String fixed = rpm.getFixed();
			String repeats = rpm.getRepeats();
			String frequency = rpm.getFrequency();
			if (fixed != null) {
				rass.setValidityType(fixed);
			} else {
				rass.setValidityType(repeats);
				Frequency frq = ruleAssApi.searchFrequency(frequency);
				rass.setFrequency(frq);
			}
			rass.setStartDate(rpm.getStartdate());
			rass.setEndDate(rpm.getEndDate());
			List<RuleAssignmentParameter> rpm1 = rpm.getRuleAssignmentParameter();
			// List<RuleAssignmentParameter> rpm4 = new ArrayList<>();
			/*
			 * Iterator it = rpm1.iterator(); while(it.hasNext()) {
			 * RuleAssignmentParameter rpm2 = (RuleAssignmentParameter)
			 * iterator.next(); RuleAssignmentParameter rpm3 = new
			 * RuleAssignmentParameter();
			 * rpm3.setParameterName(rpm2.getParameterName()); String def =
			 * rpm2.getOverwriteValue(); if (def != null) {
			 * rpm3.setOverwriteValue(rpm2.getOverwriteValue()); } else {
			 * System.out.println("logic is not ready"); } rpm4.add(rpm3); }
			 */
			rass.setRuleAssignmentParameter(rpm1);
			rp.add(rass);
		}

		long assIdForRole = assnmnt.getId();
		long assIdForEmp = assnmnt.getAssid();

		if (assIdForRole != 0) {
			RuleAssignment ruleAss = ruleAssApi.getRuleAssignment(assIdForRole);
			Role role = roleApi.searchRole(assnmnt.getName());
			ruleAss.setRuleAssignmentDetails(rp);
			ruleAss.setRole(role);
			ruleAssApi.editRuleAssignment(ruleAss);

		} else {
			RuleAssignment ruleAss = ruleAssApi.getRuleAssignment(assIdForEmp);
			Employee emp = empApi.searchEmployee(assnmnt.getEmpName());
			ruleAss.setRuleAssignmentDetails(rp);
			ruleAss.setEmployee(emp);
			ruleAssApi.editRuleAssignment(ruleAss);
		}
		return "hello";
	}

	/*-----------------------------------------------------creating compensation plan assignment ----------------------------------------------------*/
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String create(@ModelAttribute("SpringWeb") RuleListContainer1 ruleListContainer, RuleAssEmpRoleUI ruleAssEmpRole,
			ModelMap model) {
		
		model.addAttribute("text2", ruleAssEmpRole.getEmpName());
		System.out.println("EmpName: " + ruleAssEmpRole.getEmpName());
		empApi.searchEmployee(ruleAssEmpRole.getEmpName());
		List<Employee> employee = empApi.searchEmployeesByName(ruleAssEmpRole.getEmpName());
		for (Iterator iterator = employee.iterator(); iterator.hasNext();) {
			Employee emp = (Employee) iterator.next();
			long empId = emp.getId();
		}
		
		
		model.addAttribute("roleName", ruleAssEmpRole.getRoleName());
		System.out.println("Rolename: " +  ruleAssEmpRole.getRoleName());
		
		for (RuleAss1UI rul1 : ruleListContainer.getRuleList1()) {
			System.out.println("Id:   " + rul1.getId());
			System.out.println("RuleName:  " + rul1.getRuleName());
			System.out.println("Fixed:  " + rul1.getFixed());
			System.out.println("Repeats:  " + rul1.getRepeats());
			System.out.println("Frequency:  " + rul1.getFrequency());
			System.out.println("StartDate:  " + rul1.getStartdate());
			System.out.println("EndDate:  " + rul1.getEndDate());
			for (RuleAssignmentParameter rul2 : rul1.getRuleAssignmentParameter()) {
				System.out.println("ParameterName: " + rul2.getParameterName());
				System.out.println("overwriteValue: " + rul2.getOverwriteValue());
			}
		}
		List<RuleAss1UI> rul = ruleListContainer.getRuleList1();
		List<RuleAssignmentDetails> rp = new ArrayList<>();
		RuleAssignment ras = new RuleAssignment();
		Employee emp = empApi.searchEmployee(ruleAssEmpRole.getEmpName());
		ras.setEmployee(emp);
		for (Iterator iterator = rul.iterator(); iterator.hasNext();) {
			RuleAss1UI rpm = (RuleAss1UI) iterator.next();
			RuleAssignmentDetails rass = new RuleAssignmentDetails();
			rass.setId(rpm.getId());
			String rName = rpm.getRuleName();
			Rule rule1 = ruleApi.searchRuleByName(rName);
			rass.setRule(rule1);
			String fixed = rpm.getFixed();
			String repeats = rpm.getRepeats();
			String frequency = rpm.getFrequency();
			if (fixed != null) {
				rass.setValidityType(fixed);
			} else {
				rass.setValidityType(repeats);
				Frequency frq = ruleAssApi.searchFrequency(frequency);
				rass.setFrequency(frq);
			}
			rass.setStartDate(rpm.getStartdate());
			rass.setEndDate(rpm.getEndDate());
			List<RuleAssignmentParameter> rpm1 = rpm.getRuleAssignmentParameter();
			
			rass.setRuleAssignmentParameter(rpm1);
			rp.add(rass);
		}
		
		ras.setRuleAssignmentDetails(rp);
		
	 ruleAssApi.createRuleAssignment(ras);
		
		
		/*if (EmpName != null) {
			RuleAssignment ruleAss = ruleAssApi.getRuleAssignment(assIdForRole);
			//Role role = roleApi.searchRole(assnmnt.getName());
			//ruleAss.setRuleAssignmentDetails(rp);
			//ruleAss.setRole(role);
			//ruleAssApi.editRuleAssignment(ruleAss);

		} else {
			//RuleAssignment ruleAss = ruleAssApi.getRuleAssignment(assIdForEmp);
			//Employee emp = empApi.searchEmployee(assnmnt.getEmpName());
			//ruleAss.setRuleAssignmentDetails(rp);
			//ruleAss.setEmployee(emp);
			//ruleAssApi.editRuleAssignment(ruleAss);
		}*/
		
		
	
	return "hello";
}
	
	/*----------------------------------------------Search Employee-----------------------------------------------------*/
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ModelAndView showForm() {
		return new ModelAndView("SearchEmployee", "command", new Employee());
	}

	@RequestMapping(value = "/searchempimage", method = RequestMethod.POST)
	public String addempSerch(@ModelAttribute("SpringWeb") Employee obj1, ModelMap model) {
		model.addAttribute("EmployeeName", obj1.getEmployeeName());
		System.out.println("******************************" + obj1.getEmployeeName());
		List<Employee> emp = empApi.searchEmployeesByName(obj1.getEmployeeName());
		model.addAttribute("emp", emp);
		System.out.println(".........successfully submit..........");
		return "SearchEmployee";
	}

	/*-------------------------------------Take employeeName for check & pass to the /compPlanAsg-----------------------------------*/
	@RequestMapping(value = "/searchemp", method = RequestMethod.POST)
	public String empSerch(@ModelAttribute("SpringWeb") Employee obj, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) throws ServletException, IOException {
		model.addAttribute("EmployeeName", obj.getEmployeeName());
		System.out.println("#############################" + obj.getEmployeeName());
		EmployeeUI object1 = new EmployeeUI();
		object1.setEmployeeName(obj.getEmployeeName());
		request.getSession().setAttribute("EmployeeName", object1);
		return "redirect:/compplan";
		//return "redirect:/compPlanAsg";
	}

	/*-------------------------------------Take roleName for check & pass to the /compplan-----------------------------------*/
	@RequestMapping(value = "/Submitrole", method = RequestMethod.POST)
	public String addruleSerch(@ModelAttribute("SpringWeb") Role command, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) throws ServletException, IOException {
		model.addAttribute("roleName", command.getRoleName());
		RoleUI object = new RoleUI();
		object.setRoleName(command.getRoleName());
		request.getSession().setAttribute("roleName", object);
		return "redirect:/compplan";
		//return "redirect:/compPlanAsg";
	}

	/*-------------------------------------Take ruleId  pass to the /compplan-----------------------------------*/
	@RequestMapping(value = "/submitrule", method = RequestMethod.POST)
	public String addroleSerch(@ModelAttribute("SpringWeb") Rule obj2, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) throws ServletException, IOException {
		model.addAttribute("Id", obj2.getId());
		AddRuleUI object2 = new AddRuleUI();
		object2.setId(obj2.getId());
		request.getSession().setAttribute("Id", object2);
		return "redirect:/compplan";
		//return "redirect:/compPlanAsg";
	}
	
}
