package com.simpsoft.salesCommission.ui;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import com.simpsoft.salesCommission.app.UImodel.ImportId;
import com.simpsoft.salesCommission.app.UImodel.PersonListContainer;
import com.simpsoft.salesCommission.app.UImodel.PersonListContainer1;
import com.simpsoft.salesCommission.app.UImodel.QualifyingClauseUI;
import com.simpsoft.salesCommission.app.UImodel.RoleUI;
import com.simpsoft.salesCommission.app.UImodel.RuleUI;
import com.simpsoft.salesCommission.app.UImodel.TargetListContainer;
import com.simpsoft.salesCommission.app.UImodel.TargetUI;
import com.simpsoft.salesCommission.app.api.EmployeeAPI;
import com.simpsoft.salesCommission.app.api.RoleAPI;
import com.simpsoft.salesCommission.app.model.ConditionList;
import com.simpsoft.salesCommission.app.model.FieldList;
import com.simpsoft.salesCommission.app.model.OrderDetail;
import com.simpsoft.salesCommission.app.model.OrderRoster;
import com.simpsoft.salesCommission.app.model.QualifyingClause;
import com.simpsoft.salesCommission.app.model.Role;
import com.simpsoft.salesCommission.app.model.RuleParameter;
import com.simpsoft.salesCommission.app.model.Target;
import com.simpsoft.salesCommission.app.model.TargetDefinition;

@Controller
public class RoleController {
	@Autowired
	private RoleAPI roleApi;

	@Autowired
	private EmployeeAPI employeeApi;

	@RequestMapping(value = "/role", method = RequestMethod.GET)
	public String Role(ModelMap model, HttpSession session, HttpServletRequest request) {
		List<Role> roleList = roleApi.listOfRoles();
		model.addAttribute("roleList", roleList);
		

		return "orgStructure";
	}

	@RequestMapping(value = "/roleDetails/{id}", method = RequestMethod.GET)
	public String RoleDetails(@PathVariable("id") Long id, ImportId Id, HttpServletRequest request,
			HttpServletResponse response, HttpSession session, ModelMap model) {
		System.out.println(id);
		Role role = roleApi.getRole(id);
		model.addAttribute("roleDetails", role);
		if (session.getAttribute("targetListContainer") == null) {

			session.setAttribute("targetListContainer", getDummytargetListContainer1());
		}
		model.addAttribute("targetListContainer", (TargetListContainer) session.getAttribute("targetListContainer"));

		model.addAttribute("targetDefinition", employeeApi.listOfTargetDefinitions());
		
		List<Target> target = role.getTarget();
		//List<Target> targetList = new ArrayList<Target>();
		for (Iterator iterator = target.iterator(); iterator.hasNext();) {
			Target targetObj = (Target) iterator.next();
			System.out.println(targetObj.getStartDate());
		}
			model.addAttribute("targetDetails", target);
		

		return "RoleDetails";
	}

	private TargetListContainer getDummytargetListContainer1() {
		List<TargetUI> targetList = new ArrayList<TargetUI>();
		for (int i = 0; i < 1; i++) {
			targetList.add(new TargetUI());
		}
		return new TargetListContainer();
	}

	@RequestMapping(value = "/submitRoleDetails", method = RequestMethod.POST)
	public String SubmitRoleDetails(@ModelAttribute("SpringWeb") TargetUI targetUI, RoleUI roleUI,
			TargetListContainer targetListContainer, HttpSession session, ModelMap model)
					throws java.text.ParseException {

		for (TargetUI T : targetListContainer.getTargetList()) {
			System.out.println("targetName: " + T.getTargetName());
			System.out.println("startDate: " + T.getStartDate());
			System.out.println("terminationDate: " + T.getTerminationDate());
			System.out.println("Value: " + T.getValue());
		}

		model.addAttribute("roleName", roleUI.getRoleName());
		System.out.println("*************" + roleUI.getRoleName());
		model.addAttribute("description", roleUI.getDescription());
		System.out.println("*************" + roleUI.getDescription());

		Role role = roleApi.searchRoleByName(roleUI.getRoleName());
		List<TargetUI> ptr = targetListContainer.getTargetList();
		List<Target> ptr1 = new ArrayList<>();
		for (Iterator iterator = ptr.iterator(); iterator.hasNext();) {
			TargetUI targetUi = (TargetUI) iterator.next();
			Target target = new Target();
			TargetDefinition targetDeff = employeeApi.searchTargetDefinition(targetUi.getTargetName());
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			String dateInString1 = targetUi.getStartDate();
			String dateInString2 = targetUi.getTerminationDate();

			try {

				Date date1 = formatter.parse(dateInString1);
				Date date2 = formatter.parse(dateInString2);

				target.setStartDate(date1);
				target.setTerminationDate(date2);
			} catch (ParseException e) {
				e.printStackTrace();
			}

			target.setTargetDefinition(targetDeff);
			target.setValue(targetUi.getValue());
			ptr1.add(target);
		}
		role.setTarget(ptr1);
		roleApi.editRole(role);

		return "redirect:/role";
	}

}
