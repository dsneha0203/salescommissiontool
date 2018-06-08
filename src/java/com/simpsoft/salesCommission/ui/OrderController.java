package com.simpsoft.salesCommission.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.simpsoft.salesCommission.app.UImodel.DetailsId;
import com.simpsoft.salesCommission.app.UImodel.FileList;
import com.simpsoft.salesCommission.app.UImodel.ImportId;
import com.simpsoft.salesCommission.app.UImodel.Person1;
import com.simpsoft.salesCommission.app.UImodel.PersonListContainer;
import com.simpsoft.salesCommission.app.UImodel.PersonListContainer1;
import com.simpsoft.salesCommission.app.UImodel.PersonListContainer2;
import com.simpsoft.salesCommission.app.UImodel.QualifyingClauseUI;
import com.simpsoft.salesCommission.app.UImodel.RuleUI;
import com.simpsoft.salesCommission.app.UImodel.TargetListContainer;
import com.simpsoft.salesCommission.app.UImodel.TargetUI;
import com.simpsoft.salesCommission.app.api.EmployeeAPI;
import com.simpsoft.salesCommission.app.api.OrderAPI;
import com.simpsoft.salesCommission.app.api.RuleSimpleAPI;
import com.simpsoft.salesCommission.app.api.SplitRuleAPI;
import com.simpsoft.salesCommission.app.model.OrderDetail;
import com.simpsoft.salesCommission.app.model.OrderRoster;
import com.simpsoft.salesCommission.app.model.QualifyingClause;
import com.simpsoft.salesCommission.app.model.RuleParameter;
import com.simpsoft.salesCommission.app.model.SplitQualifyingClause;
import com.simpsoft.salesCommission.app.model.SplitRule;
import com.simpsoft.salesCommission.app.model.SplitRuleBeneficiary;
import com.simpsoft.salesCommission.app.model.OrderLineItems;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class OrderController {

	@Autowired
	private OrderAPI orderApi;
	
	@Autowired
	private RuleSimpleAPI ruleSimpleApi;
	
	@Autowired
	private SplitRuleAPI splitRuleApi;

	// private static final Logger logger =
	// Logger.getLogger(EmployeeController.class);

	@RequestMapping(value = "/importOrders", method = RequestMethod.GET)
	public String ImportOrder(ModelMap model, HttpSession session, HttpServletRequest request) {
		List<OrderRoster> orm = orderApi.listOfOrderRosters();
		model.addAttribute("listOrder", orm);

		return "Orders";
	}

	private void msgbox(String s) {
		JOptionPane.showMessageDialog(null, s);
	}

	@RequestMapping(value = "/savefiles", method = RequestMethod.POST)
	public String crunchifySave(@ModelAttribute("uploadForm") FileList uploadForm, Model map)
			throws IllegalStateException, IOException {
		//String saveDirectory = "c:/NewFile/";

		List<MultipartFile> fileList = uploadForm.getFiles();

		List<String> fileNames = new ArrayList<String>();

		if (null != fileList && fileList.size() > 0) {
			for (MultipartFile multipartFile : fileList) {

				String fileName = multipartFile.getOriginalFilename();
				if (!"".equalsIgnoreCase(fileName)) {
					// System.out.println(multipartFile.getInputStream());
					System.out.println(multipartFile.getSize());
					//multipartFile.transferTo(new File(saveDirectory + fileName));
					orderApi.importOrders(multipartFile.getInputStream());
				}
			}
		}

		map.addAttribute("files", fileNames);
		return "redirect:/importOrders";
	}

	@RequestMapping(value = "/orderDetails/{id}", method = RequestMethod.GET)
	public String OrderDetails(@PathVariable("id") int id, ImportId Id, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) throws ServletException, IOException {
		//model.addAttribute("importId", Id.getImportId());
		//int id = Id.getImportId();
		request.getSession().setAttribute("id",id);
		OrderRoster ors = orderApi.getOrderRoster(id);
		List<OrderDetail> ord = ors.getOrderDetail();
		List<OrderDetail> ord1 = new ArrayList<OrderDetail>();
		Iterator it = ord.iterator();
		while (it.hasNext()) {
			OrderDetail ord2 = (OrderDetail) it.next();
			System.out.println(ord2.getId());
			System.out.println(ord2.getOrderDate());
			ord1.add(ord2);
		}
		model.addAttribute("ordetails", ord1);
		return "OrderDetails";
	}
	
	@RequestMapping(value = "/verifyOrder/{id}", method = RequestMethod.GET)
	public String VerifyOrder(@PathVariable("id") int id, ImportId Id, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) throws ServletException, IOException {
		
		OrderRoster ors = orderApi.getOrderRoster(id);
		splitRuleApi.assignSplitRule(ors);
		return "redirect:/importOrders";
	}

	@RequestMapping(value = "/lineItemDetails/{id}", method = RequestMethod.GET)
	public String OrderLineItems(@PathVariable("id") int id, DetailsId Id, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		OrderDetail ord1 = orderApi.getOrderDetail(id);
		List<OrderLineItems> ordLi = ord1.getOrderLineItems();
		List<OrderLineItems> ordLi2 = new ArrayList<OrderLineItems>();

		Iterator it = ordLi.iterator();
		while (it.hasNext()) {
			OrderLineItems ordLi1 = (OrderLineItems) it.next();
			System.out.println(ordLi1.getDiscountPercentage());
			System.out.println(ordLi1.getDutyPercentage());
			ordLi2.add(ordLi1);
		}
		model.addAttribute("OrderLineItems", ordLi2);
		return "LineItems";

	}

	@RequestMapping(value = "/splitDetails/{id}", method = RequestMethod.GET)
	public String SplitOrders(@PathVariable("id") int id, ModelMap model, HttpSession session, HttpServletRequest request) {
		
		session.setAttribute("personListContainer1", getDummyPersonListContainer1());
		session.setAttribute("personListContainer", getDummyPersonListContainer());
		
	

		model.addAttribute("listRule2", ruleSimpleApi.listOfFields());
		model.addAttribute("listRule3", ruleSimpleApi.listOfConditions());
		
		

		return "splitOrders";
	}
	
	private PersonListContainer1 getDummyPersonListContainer1() {
		List<QualifyingClauseUI> personList = new ArrayList<QualifyingClauseUI>();
			personList.add(new QualifyingClauseUI());
		return new PersonListContainer1();
	}
	
	private PersonListContainer getDummyPersonListContainer() {
		List<RuleParameter> personList1 = new ArrayList<RuleParameter>();
		
			personList1.add(new RuleParameter());
	
		return new PersonListContainer(personList1);
	}
	
	
	@RequestMapping(value = "/orderSplitRule", method = RequestMethod.GET)
	public String OrderSplitRule(ImportId Id, HttpServletRequest request,
			HttpServletResponse response, ModelMap model){
		
		List<SplitRule> splitRule = splitRuleApi.listOfSplitRule();
		model.addAttribute("splitRuleDetails", splitRule);
		

		
		return "OrderSplitRule";
	}
	
	
	@RequestMapping(value = "/splitRuleDetails/{id}", method = RequestMethod.GET)
	public String SplitRuleDetails(@PathVariable("id") int id, ModelMap model, HttpSession session, HttpServletRequest request) {
		
		session.setAttribute("personListContainer1", getDummyPersonListContainer1());
		session.setAttribute("personListContainer", getDummyPersonListContainer());
		
		
		
		SplitRule splitRule = splitRuleApi.getSplitRule(id);
		List<SplitQualifyingClause> qualifyingList = splitRule.getSplitQualifyingClause();
		model.addAttribute("qualifyingList", qualifyingList);
		model.addAttribute("splitRuleDetails", splitRule);
		List<SplitRuleBeneficiary> beneficiary = splitRule.getSplitRuleBeneficiary();
		model.addAttribute("beneficiary", beneficiary);

		
		
		model.addAttribute("listRule2", ruleSimpleApi.listOfFields());
		model.addAttribute("listRule3", ruleSimpleApi.listOfConditions());
		
		

		return "splitOrders";
	}
	
	
}
