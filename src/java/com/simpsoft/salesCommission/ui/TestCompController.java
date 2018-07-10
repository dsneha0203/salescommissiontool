package com.simpsoft.salesCommission.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.JOptionPane;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import com.simpsoft.salesCommission.app.UImodel.AddRuleUI;
import com.simpsoft.salesCommission.app.UImodel.AssignmentUI;
import com.simpsoft.salesCommission.app.UImodel.EmployeeUI;
import com.simpsoft.salesCommission.app.UImodel.RoleUI;
import com.simpsoft.salesCommission.app.UImodel.RuleAss1UI;
import com.simpsoft.salesCommission.app.UImodel.RuleAssEmpRoleUI;
import com.simpsoft.salesCommission.app.UImodel.RuleAssParamUI;
import com.simpsoft.salesCommission.app.UImodel.RuleAssUI;
import com.simpsoft.salesCommission.app.UImodel.RuleListContainer;
import com.simpsoft.salesCommission.app.UImodel.RuleListContainer1;
import com.simpsoft.salesCommission.app.UImodel.RuleListParamContainer;
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
import com.simpsoft.salesCommission.app.model.TargetDefinition;

import org.apache.log4j.Logger;

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
	
	
	
	
	
	private static final Logger logger = Logger.getLogger(TestCompController.class);
	
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
		
		
RoleUI object = (RoleUI) request.getSession().getAttribute("roleNameCompAsg");
		
		if (object != null) {
			logger.debug("ROLE UI SESSION ATTRIBUTE= "+object.getRoleName());
			Role roleasign = roleApi.searchRoleByName(object.getRoleName());
			model.addAttribute("rlelist", roleasign);
			logger.debug("THE VALUE IS= " + roleasign.getRoleName());
			
			
			
			
			
			
			
			RuleAssignment rAssdtail = ruleAssApi.searchAssignedRule(object.getRoleName());
			if(rAssdtail != null) {
			List<RuleAssignmentDetails> ptr1 = rAssdtail.getRuleAssignmentDetails();
			if(ptr1 != null) {
			ArrayList<RuleAssignmentDetails> rl1 = new ArrayList<RuleAssignmentDetails>();
			//ArrayList<Role> rleName = new ArrayList<Role>();
			model.addAttribute("assObj", rAssdtail);
			
			//List<RuleAssignmentParameter> parameterList = new ArrayList<RuleAssignmentParameter>();
			
			Iterator<RuleAssignmentDetails> it1 = ptr1.iterator();
			while (it1.hasNext()) {
				
				RuleAssignmentDetails rp = (RuleAssignmentDetails) it1.next();
				List<RuleAssignmentParameter> ptr2 = rp.getRuleAssignmentParameter();
				logger.debug("RULE NAME= "+rp.getRule().getRuleName());
				logger.debug("RULE ID= "+rp.getRule().getId());
				rl1.add(rp);
			}
			model.addAttribute("List2", rl1);
			
			status.setComplete();
			session.removeAttribute("roleNameCompAsg");
			return new ModelAndView("compPlanAssignment");
			//return new ModelAndView("CompPlan");
					}
				}else {
				//return new ModelAndView("CompPlan");
					return new ModelAndView("compPlanAssignment");
				
				}
			
			
			
			
			}
	
		/*-----------------------------------Finding compensation assignment for Employee------------------------------------*/
		
		
		EmployeeUI object1 = (EmployeeUI) request.getSession().getAttribute("EmployeeName");

		if (object1 != null) {
			Employee empChosen = empApi.searchEmployee(object1.getEmployeeName());
			if(empChosen != null) {
			model.addAttribute("empNameChosen",empChosen.getEmployeeName());
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
			//return new ModelAndView("CompPlan");
			return new ModelAndView("compPlanAssignment");
			}
			}else {
				//return new ModelAndView("CompPlan");
				return new ModelAndView("compPlanAssignment");
			}
			
			}else {
				JOptionPane.showMessageDialog(null, 
                        "This employee does not exist in the database!", 
                        "Cannot find employee", 
                        JOptionPane.WARNING_MESSAGE);
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
			//return new ModelAndView("CompPlan");
			return new ModelAndView("compPlanAssignment");
			
		}
		//return new ModelAndView("CompPlan");
		return new ModelAndView("compPlanAssignment");
		
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
	public String update(AssignmentUI assnmnt, RuleListContainer ruleListContainer,
			ModelMap model, HttpSession session, HttpServletRequest request) {
		logger.debug("-----IN UPDATE METHOD----");
		
		
		model.addAttribute("id", assnmnt.getId());
		logger.debug("chkId: " + assnmnt.getId());
		model.addAttribute("assRoleName", assnmnt.getName());
		logger.debug("Assignee name: " + assnmnt.getName());
		model.addAttribute("assid", assnmnt.getAssid());
		logger.debug("chkId: " + assnmnt.getAssid());
		model.addAttribute("assempName", assnmnt.getEmpName());
		logger.debug("name: " + assnmnt.getEmpName());

		for (RuleAssUI rul1 : ruleListContainer.getRuleList()) {
			logger.debug("Id:   " + rul1.getId());
			logger.debug("RuleName:  " + rul1.getRuleName());
			logger.debug("Fixed:  " + rul1.getFixed());
			logger.debug("Repeats:  " + rul1.getRepeats());
			logger.debug("Frequency:  " + rul1.getFrequency());
			logger.debug("StartDate:  " + rul1.getStartdate());
			logger.debug("EndDate:  " + rul1.getEndDate());
			for (RuleAssignmentParameter rul2 : rul1.getRuleAssignmentParameter()) {
				logger.debug("ParameterName: " + rul2.getParameterName());
				logger.debug("OverwriteValue: " + rul2.getOverwriteValue());
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
			List<RuleAssignmentParameter> ruleParamList = new ArrayList<>();
			for(Iterator iterator1 = rpm1.iterator(); iterator1.hasNext();) {
				RuleAssignmentParameter ruleAssParam = (RuleAssignmentParameter) iterator1.next();
				String paramValue = (ruleAssApi.getParamValue(ruleAssParam.getParameterName())).getParameterValue();
				logger.debug("PARAM VALUE= "+paramValue);
				logger.debug("OVERWRITE ="+ruleAssParam.getOverwriteValue());
				if(!ruleAssParam.getTargetDefinition().getDisplayName().equals("")) {
					logger.debug("TARGET DEF NAME= "+ruleAssParam.getTargetDefinition().getDisplayName());
				}else {
					logger.debug("TARGET DEF NAME= null");
				}
				if(ruleAssParam.getOverwriteValue().equals("")) {
					logger.debug("VALUE TYPE= DEFAULT");
					ruleAssParam.setValueType("default");
					ruleAssParam.setOverwriteValue(paramValue);
					
					if( ruleAssParam.getTargetDefinition().getDisplayName().equals("")){
						ruleAssParam.setTargetDefinition(null);
					}else {
						logger.debug("OVERLAY VALUE= "+ruleAssParam.getTargetDefinition().getDisplayName());
						logger.debug("VALUE TYPE= OVERLAY");
						ruleAssParam.setValueType("overlay");
						TargetDefinition tgd = ruleAssApi.searchTargetDef(ruleAssParam.getTargetDefinition().getDisplayName());
						ruleAssParam.setTargetDefinition(tgd);
					}
				}
				else if(!(ruleAssParam.getOverwriteValue().equals(paramValue)) && ruleAssParam.getTargetDefinition().getDisplayName().equals("")) {
					logger.debug("OVERWRITE VALUE= "+ruleAssParam.getOverwriteValue());
					logger.debug("VALUE TYPE= OVERWRITE");
					ruleAssParam.setValueType("overwrite");
					ruleAssParam.setTargetDefinition(null);
				}
				else if( !(ruleAssParam.getTargetDefinition().getDisplayName().equals(""))) {
					logger.debug("OVERLAY VALUE= "+ruleAssParam.getTargetDefinition().getDisplayName());
					logger.debug("VALUE TYPE= OVERLAY");
					ruleAssParam.setValueType("overlay");
					TargetDefinition tgd = ruleAssApi.searchTargetDef(ruleAssParam.getTargetDefinition().getDisplayName());
					ruleAssParam.setTargetDefinition(tgd);
				}
				
				
				ruleParamList.add(ruleAssParam);
				
			}
						
			//rass.setRuleAssignmentParameter(rpm1);
			rass.setRuleAssignmentParameter(ruleParamList);
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
		
		return "redirect:/compplan";
		
	}

	/*-----------------------------------------------------creating compensation plan assignment ----------------------------------------------------*/
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String create(@ModelAttribute("SpringWeb") RuleListContainer1 ruleListContainer, RuleAssEmpRoleUI ruleAssEmpRole,ModelMap model, 
			HttpServletRequest request, HttpSession session,SessionStatus status) {
		
		model.addAttribute("text2", ruleAssEmpRole.getEmpName());
		logger.debug("EmpName: " + ruleAssEmpRole.getEmpName());
//		if(!ruleAssEmpRole.getEmpName().equals("")) {
//		empApi.searchEmployee(ruleAssEmpRole.getEmpName());
//		List<Employee> employee = empApi.searchEmployeesByName(ruleAssEmpRole.getEmpName());
//		for (Iterator iterator = employee.iterator(); iterator.hasNext();) {
//			Employee emp = (Employee) iterator.next();
//			long empId = emp.getId();
//		}
//		}
		
		model.addAttribute("roleNameCompAsg", ruleAssEmpRole.getRoleName());
		logger.debug("Rolename: " +  ruleAssEmpRole.getRoleName());
		
		for (RuleAss1UI rul1 : ruleListContainer.getRuleList1()) {
			logger.debug("Id:   " + rul1.getId());
			logger.debug("RuleName:  " + rul1.getRuleName());
			logger.debug("Fixed:  " + rul1.getFixed());
			logger.debug("Repeats:  " + rul1.getRepeats());
			logger.debug("Frequency:  " + rul1.getFrequency());
			logger.debug("StartDate:  " + rul1.getStartdate());
			logger.debug("EndDate:  " + rul1.getEndDate());
			for (RuleAssignmentParameter rul2 : rul1.getRuleAssignmentParameter()) {
				logger.debug("ParameterName: " + rul2.getParameterName());
				logger.debug("overwriteValue: " + rul2.getOverwriteValue());
				if(!rul2.getTargetDefinition().getDisplayName().equals("")) {
					logger.debug("target def name: "+rul2.getTargetDefinition().getDisplayName());
				}
			}
		}
		List<RuleAss1UI> rul = ruleListContainer.getRuleList1();
		List<RuleAssignmentDetails> rp = new ArrayList<>();
		RuleAssignment ras = new RuleAssignment();
		if(!ruleAssEmpRole.getEmpName().equals("")) {
			Employee emp = empApi.searchEmployee(ruleAssEmpRole.getEmpName());			
			ras.setEmployee(emp);
			ras.setRole(null);
			logger.debug("EMPLOYEE ID ASSIGNED= "+ras.getEmployee().getId());
		}else {
			Role role = roleApi.searchRoleByName(ruleAssEmpRole.getRoleName());
			ras.setRole(role);
			ras.setEmployee(null);
			logger.debug("ROLE ID ASSIGNED= "+ras.getRole().getId());
		}
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
				logger.debug("VALIDITY TYPE ASSIGNED= "+rass.getValidityType());
				
			}
			rass.setStartDate(rpm.getStartdate());
			logger.debug("START DATE ASSIGNED= "+rass.getStartDate());
			rass.setEndDate(rpm.getEndDate());
			logger.debug("END DATE ASSIGNED= "+rass.getEndDate());
			
			List<RuleAssignmentParameter> rpm1 = rpm.getRuleAssignmentParameter();
			
			List<RuleAssignmentParameter> ruleParamList = new ArrayList<>();
			for(Iterator iterator1 = rpm1.iterator(); iterator1.hasNext();) {
				RuleAssignmentParameter ruleAssParam = (RuleAssignmentParameter) iterator1.next();
				String paramValue = (ruleAssApi.getParamValue(ruleAssParam.getParameterName())).getParameterValue();
				logger.debug("PARAM VALUE= "+paramValue);
				logger.debug("OVERWRITE ="+ruleAssParam.getOverwriteValue());
				if(!ruleAssParam.getTargetDefinition().getDisplayName().equals("")) {
					logger.debug("TARGET DEF NAME= "+ruleAssParam.getTargetDefinition().getDisplayName());
				}else {
					logger.debug("TARGET DEF NAME= null");
				}
				if(ruleAssParam.getOverwriteValue().equals("")) {
					logger.debug("VALUE TYPE= DEFAULT");
					ruleAssParam.setValueType("default");
					ruleAssParam.setOverwriteValue(paramValue);
					
					if( ruleAssParam.getTargetDefinition().getDisplayName().equals("")){
						ruleAssParam.setTargetDefinition(null);
					}else {
						logger.debug("OVERLAY VALUE= "+ruleAssParam.getTargetDefinition().getDisplayName());
						logger.debug("VALUE TYPE= OVERLAY");
						ruleAssParam.setValueType("overlay");
						TargetDefinition tgd = ruleAssApi.searchTargetDef(ruleAssParam.getTargetDefinition().getDisplayName());
						ruleAssParam.setTargetDefinition(tgd);
					}
				}
				else if(!(ruleAssParam.getOverwriteValue().equals(paramValue)) && ruleAssParam.getTargetDefinition().getDisplayName().equals("")) {
					logger.debug("OVERWRITE VALUE= "+ruleAssParam.getOverwriteValue());
					logger.debug("VALUE TYPE= OVERWRITE");
					ruleAssParam.setValueType("overwrite");
					ruleAssParam.setTargetDefinition(null);
				}
				else if( !(ruleAssParam.getTargetDefinition().getDisplayName().equals(""))) {
					logger.debug("OVERLAY VALUE= "+ruleAssParam.getTargetDefinition().getDisplayName());
					logger.debug("VALUE TYPE= OVERLAY");
					ruleAssParam.setValueType("overlay");
					TargetDefinition tgd = ruleAssApi.searchTargetDef(ruleAssParam.getTargetDefinition().getDisplayName());
					ruleAssParam.setTargetDefinition(tgd);
				}
				
				
				ruleParamList.add(ruleAssParam);
				
			}
						
			//rass.setRuleAssignmentParameter(rpm1);
			rass.setRuleAssignmentParameter(ruleParamList);
			rp.add(rass);
		}
		
		ras.setRuleAssignmentDetails(rp);
		if(!ruleAssEmpRole.getEmpName().equals("")) {
			Employee emp = empApi.searchEmployee(ruleAssEmpRole.getEmpName());
			if(ruleAssApi.getPreviousRuleAssignmentForEmp(emp.getId()) !=null) {
			//get rule assignment id of this emp id
			long existingRuleAsgId = ruleAssApi.getPreviousRuleAssignmentForEmp(emp.getId()).getId();
			logger.debug("EXISTING ID FOR EMP= "+existingRuleAsgId);
			ruleAssApi.editRuleAssignment(ras,existingRuleAsgId);
			}else {
				ruleAssApi.createRuleAssignment(ras);
			}
		}else {
			Role role = roleApi.searchRoleByName(ruleAssEmpRole.getRoleName());
			logger.debug("ROLE SELECTED= "+role.getRoleName());
			//get rule assignment id of this role id
			if(ruleAssApi.getPreviousRuleAssignmentForRole(role.getId()) !=null) {
				
				long existingRuleAsgId = ruleAssApi.getPreviousRuleAssignmentForRole(role.getId()).getId();
				logger.debug("EXISTING ID FOR ROLE= "+existingRuleAsgId);
				ruleAssApi.editRuleAssignment(ras,existingRuleAsgId);
				
			}else {
				ruleAssApi.createRuleAssignment(ras);
			}
		}
			
	
		
		
		
//		if (EmpName != null) {
//			RuleAssignment ruleAss = ruleAssApi.getRuleAssignment(assIdForRole);
//			//Role role = roleApi.searchRole(assnmnt.getName());
//			//ruleAss.setRuleAssignmentDetails(rp);
//			//ruleAss.setRole(role);
//			//ruleAssApi.editRuleAssignment(ruleAss);
//		} else {
//			//RuleAssignment ruleAss = ruleAssApi.getRuleAssignment(assIdForEmp);
//			//Employee emp = empApi.searchEmployee(assnmnt.getEmpName());
//			//ruleAss.setRuleAssignmentDetails(rp);
//			//ruleAss.setEmployee(emp);
//			//ruleAssApi.editRuleAssignment(ruleAss);
//		}
		
//		logger.debug("EMPLOYEE NAME= "+ruleAssEmpRole.getEmpName());
//		logger.debug("ROLE NAME= "+ruleAssEmpRole.getRoleName());
	
	return "redirect:/compplan";
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
		
	}

	/*-------------------------------------Take roleNameCompAsg for check & pass to the /compplan-----------------------------------*/
	@RequestMapping(value = "/Submitrole", method = RequestMethod.POST)
	public String addruleSerch(@ModelAttribute("SpringWeb") Role command, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) throws ServletException, IOException {
		model.addAttribute("roleNameCompAsg", command.getRoleName());
		logger.debug("ROLE NAME= "+command.getRoleName());
		RoleUI object = new RoleUI();
		object.setRoleName(command.getRoleName());
		request.getSession().setAttribute("roleNameCompAsg", object);
		return "redirect:/compplan";
		
	}

	/*-------------------------------------Take ruleId  pass to the /compplan-----------------------------------*/
	@RequestMapping(value = "/submitrule", method = RequestMethod.POST)
	public String addNewRule(@ModelAttribute("SpringWeb") Rule obj2, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) throws ServletException, IOException {
		model.addAttribute("Id", obj2.getId());
		AddRuleUI object2 = new AddRuleUI();
		object2.setId(obj2.getId());
		request.getSession().setAttribute("Id", object2);
		return "redirect:/compplan";
		
	}
	
}