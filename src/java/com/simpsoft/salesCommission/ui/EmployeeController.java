package com.simpsoft.salesCommission.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.AsyncContext;
import javax.servlet.DispatcherType;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import com.simpsoft.salesCommission.app.UImodel.EmployeeUI;
import com.simpsoft.salesCommission.app.UImodel.PersonListContainer;
import com.simpsoft.salesCommission.app.UImodel.PersonListContainer1;
import com.simpsoft.salesCommission.app.UImodel.RoleUI;
import com.simpsoft.salesCommission.app.UImodel.RuleUI;
import com.simpsoft.salesCommission.app.UImodel.TargetListContainer;
import com.simpsoft.salesCommission.app.UImodel.TargetUI;
import com.simpsoft.salesCommission.app.api.EmployeeAPI;
import com.simpsoft.salesCommission.app.api.RoleAPI;
import com.simpsoft.salesCommission.app.api.RuleAPI;
import com.simpsoft.salesCommission.app.model.Employee;
import com.simpsoft.salesCommission.app.model.EmployeeManagerMap;
import com.simpsoft.salesCommission.app.model.EmployeeRoleMap;
import com.simpsoft.salesCommission.app.model.OfficeLocation;
import com.simpsoft.salesCommission.app.model.Role;

@Controller
public class EmployeeController {

	@Autowired
	private EmployeeAPI employeeApi;
	@Autowired
	private RuleAPI ruleApi;

	@Autowired
	private RoleAPI roleApi;

	private static final Logger logger = Logger.getLogger(EmployeeController.class);

	@RequestMapping(value = "/employeeList", method = RequestMethod.GET)
	public String simpleRule(ModelMap model, HttpSession session, HttpServletRequest request) {
		List<Employee> emp = employeeApi.listEmployees();
		for (Employee employee : emp) {
			long empId = employee.getId();
			System.out.println("EMP ID: "+empId);
			String empName = employee.getEmployeeName();
			System.out.println("EMP NAME: "+empName);
			
			Employee emp1 = employeeApi.getEmployee(empId);
			
			request.getSession().setAttribute("empDetailsId", empId);
			
			List<EmployeeManagerMap> manager = emp1.getEmployeeManagerMap();
			List<EmployeeRoleMap> role = emp1.getEmployeeRoleMap();
			for (Iterator iterator = role.iterator(); iterator.hasNext();) {
				EmployeeRoleMap empRoleMap = (EmployeeRoleMap) iterator.next();
				Role role1 = empRoleMap.getRole();
				String roleName = role1.getRoleName();
				model.addAttribute("roleName"+empId, roleName);
				
			}

			for (Iterator iterator = manager.iterator(); iterator.hasNext();) {
				EmployeeManagerMap managerMap = (EmployeeManagerMap) iterator.next();
				Employee managerName = managerMap.getManager();
				String mName = managerName.getEmployeeName();
				long managerId = managerName.getId();
				model.addAttribute("managerName"+empId, mName);
				model.addAttribute("managerId"+empId, managerId);
			}
		}
		
		model.addAttribute("empList", emp);
		
		System.out.println(".......servlet running.......");
		return "employeeList";
	}

	@RequestMapping("/empDetails/{id}")
	public String EmployeeDetails(@PathVariable("id") int id, Employee employee, ModelMap model,
			HttpServletRequest request1, HttpSession session2, HttpServletResponse response)
					throws ServletException, IOException {

		session2.setAttribute("targetListContainer", getDummytargetListContainer1());
		HttpSession session1 = request1.getSession(false);
		if (session1 != null)
			session1.invalidate();
		Employee emp = employeeApi.getEmployee(id);
		request1.getSession().setAttribute("empDetailsId", id);
		model.addAttribute("emp", emp);
		List<EmployeeManagerMap> manager = emp.getEmployeeManagerMap();
		List<EmployeeRoleMap> role = emp.getEmployeeRoleMap();
		for (Iterator iterator = role.iterator(); iterator.hasNext();) {
			EmployeeRoleMap empRoleMap = (EmployeeRoleMap) iterator.next();
			Role role1 = empRoleMap.getRole();
			String roleName = role1.getRoleName();
			request1.getSession().setAttribute("roleName", roleName);
		}

		for (Iterator iterator = manager.iterator(); iterator.hasNext();) {
			EmployeeManagerMap managerMap = (EmployeeManagerMap) iterator.next();
			Employee managerName = managerMap.getManager();
			String mName = managerName.getEmployeeName();
			request1.getSession().setAttribute("managerName", mName);
		}

		model.addAttribute("targetDefinition", employeeApi.listOfTargetDefinitions());
		model.addAttribute("roleMap", role);
		model.addAttribute("managerMap", manager);
		model.addAttribute("targetDefinition", employeeApi.listOfTargetDefinitions());

		return "empDetails";
	}

	private TargetListContainer getDummytargetListContainer1() {
		List<TargetUI> targetList = new ArrayList<TargetUI>();
		for (int i = 0; i < 1; i++) {
			targetList.add(new TargetUI());
		}
		return new TargetListContainer();
	}

	@RequestMapping("/showAllRoles/{id}")
	public String ShowAllRoles(@PathVariable("id") int id, Employee employee, ModelMap model) {
		Employee emp = employeeApi.getEmployee(id);
		List<EmployeeRoleMap> role = emp.getEmployeeRoleMap();
		model.addAttribute("roleDetails", role);

		return "empRoleDetails";
	}

	@RequestMapping("/setEndDate")
	public String SetEndDate(Employee employee, ModelMap model) {
		return "SetEndDate";
	}

//	 @RequestMapping(value = "/submitEndDate/{id}", method = RequestMethod.POST)
//	public String SetEndDate(@PathVariable("id") int id, RuleUI ruleUI, ModelMap model) {
//
//		model.addAttribute("setDate", ruleUI.getSetDate());
//		Date endDate = ruleUI.getSetDate();
//		System.out.println("====CALLING SET END DATE METHOD OF EMP API====");
//		employeeApi.setEndDate(endDate, id);
//
//		return "redirect:/showAllRoles/{id}";
//
//	}
	
	@RequestMapping(value = "/submitenddate/{id}", method = RequestMethod.POST)
	public String setEndDate(@PathVariable("id") int id, RuleUI ruleUI, ModelMap model){
		logger.debug("_______________________________________________________________");
		logger.debug("====CALLING SET END DATE METHOD OF EMP API====");
		logger.debug("ruleUI.getSetDate()= "+ruleUI.getSetDate());
		Date endDate = ruleUI.getSetDate();
		logger.debug("====CALLING SET END DATE METHOD OF EMP API====");
		employeeApi.setEndDate(endDate, id);
		return "redirect:/showAllRoles/{id}";
	}

	@RequestMapping("/selectRole")
	public String SelectRole(ModelMap model, HttpSession session, HttpServletRequest request) {

		List<Role> roleList = roleApi.listOfRoles();
		model.addAttribute("roleList", roleList);
		return "SelectRole";

	}

	@RequestMapping(value = "/submitNewRole/{id}", method = RequestMethod.POST)
	public String SubmitSelectRole(@PathVariable("id") int id, RoleUI roleUI, ModelMap model,
			HttpServletRequest request2, HttpSession session2, HttpServletResponse response)
					throws ServletException, IOException {
		model.addAttribute("selectRole", roleUI.getSelectRole());
		String selectRoleName = roleUI.getSelectRole();
		long empId = id;
		employeeApi.createEmployeeRoleMap(selectRoleName, empId);
		return "redirect:/empDetails/{id}";

	}

	@RequestMapping("/selectManager")
	public String SelectManager(ModelMap model, HttpSession session, HttpServletRequest request) {
		return "SelectEmpForManager";

	}

	@RequestMapping(value = "/submitEmpManager", method = RequestMethod.POST)
	public String addempSerch(@ModelAttribute("SpringWeb") Employee obj1, ModelMap model) {
		model.addAttribute("EmployeeName", obj1.getEmployeeName());
		List<Employee> emp = employeeApi.searchEmployeesByName(obj1.getEmployeeName());
		model.addAttribute("emp", emp);
		return "SelectEmpForManager";
	}
	
	@RequestMapping(value = "/submitCurrentManager/{id}", method = RequestMethod.POST)
	public String SubmitCurrentManager(@PathVariable("id") int id, EmployeeUI employeeUi, ModelMap model,
			HttpServletRequest request2, HttpSession session2, HttpServletResponse response)
					throws ServletException, IOException {
		model.addAttribute("currentManager", employeeUi.getCurrentManager());
		String currentmanager = employeeUi.getCurrentManager();
		long empId = id;
		employeeApi.createEmployeeManagerMap(currentmanager, empId);
		return "redirect:/empDetails/{id}";

	}

	@RequestMapping("/showAllManagers/{id}")
	public String ShowAllManagers(@PathVariable("id") int id, Employee employee, ModelMap model) {
		Employee emp = employeeApi.getEmployee(id);
		List<EmployeeManagerMap> managers = emp.getEmployeeManagerMap();
		model.addAttribute("managers", managers);

		return "empManagerDetails";

	}

}
