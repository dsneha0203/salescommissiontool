<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

<tiles:insertDefinition name="defaultTemplate">
<tiles:putAttribute name="body">
<head>
	<title>Compensation Plan Assignment</title>
	<link rel="stylesheet" href="resources/css/style.css" />
</head>
<style>
table, th, td {
    padding: 5px;
}
</style>
<script>
window.onload= function() {
	document.getElementById('emp_div').style.display = "none";
	document.getElementById('emp_name').style.display = "none";
	document.getElementById('role_div').style.display = "none";
};
/*----------------------------Script for window popup -------------------------- */ 
function openWindow(){
	window.open('/CommissionTool/search' , '' , 'width=800,height=700,scrollbars=yes');
	document.getElementById('emp_name').style.display = "block";
	}

function roleChanged(){
	
	if(document.getElementById('radio_role').checked){
		document.getElementById('radio_emp').checked = false;
		document.getElementById('emp_div').style.display = "none";
		document.getElementById('emp_name').style.display = "none";
		document.getElementById('role_div').style.display = "block";
		empChanged();
	}
	
}

function empChanged(){
	if((document.getElementById('radio_emp')).checked){
		document.getElementById('emp_div').style.display = "block";
		document.getElementById('radio_role').checked = false;
		roleChanged();
		document.getElementById('role_div').style.display = "none";
	}
}

</script>
<br><br>
		<h2 align="center">Compensation Plan Assignment</h2>
		
<center>	
<!-- ------------------------------------------This part is for choosing employee or role----------------------------------- -->
<div>	
		<table width="800px">
		<tr>
		<td width="400px">
			<input type="radio" id="radio_emp" value="emp" onchange="empChanged()">&nbsp;<strong>Employee</strong>	
			&nbsp;&nbsp;&nbsp;
			&nbsp;&nbsp;&nbsp;
		</td>	
		<td width="400px" >
  			<input type="radio" id="radio_role" value="role" onchange="roleChanged()">&nbsp;<strong>Role</strong>
		</td>
		</tr>
		<tr>
		<td width="400px" >
		<div id="emp_div">Select Employee:&nbsp;&nbsp; <input type="image" img src="resources/image/search.png"
							style="height: 30px; width: 30px;" 
							 onclick="openWindow()" />
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</div>
		<form id="form1" action="/CommissionTool/searchemp" method="post">
							<div id="emp_name">
								Selected Employee:&nbsp;&nbsp; <input type="text" name="EmployeeName"
									id="text1" name="for_radio1[]" class="radio1" disabled="true"
									size="11"
									<c:forEach items="${empList}"
													var="emp">value="${emp.employeeName }"</c:forEach> />
								&nbsp;&nbsp;<input type="submit" value="Check plans" id="btn" />
								</div>
								
		</form>
		</td>
		<td>
		<div id="role_div">
		<form action="/CommissionTool/Submitrole" method="post" model="command">
		Select role:&nbsp;&nbsp;
		<select name="roleName" class="radio2" name="for_radio2[]"
				 style="width: 140px; height: 1.5em;">
					
						<c:forEach items="${listRole}" var="role">
							<option value="${role.roleName}">
							<c:out value="${role.roleName}" />
							</option>
						</c:forEach>
		</select> <input type="submit" value="check">
		</form>
		</div>
		</td>
		</tr>
		</table>
</div>

	<!-- ------------------------------------------This part is for displaying table against Employee/Role----------------------------------- -->

<br><br>
	<div>
		<h3 align="center">Selected Rule(s)</h3>
		<table border="1">
			<!-- <thead>
				<tr>
							<td>Rule Id</td>
							<td>Rule Details</td>
							<td>Fixed</td>
							<td>Repeats</td>
							<td>From</td>
							<td>To</td>
							<td>&nbsp;Parameter&nbsp;|&nbsp;&nbsp;Default value&nbsp;&nbsp;|&nbsp;&nbsp;Overwrite</td>
				</tr>
			</thead> -->
			
			<thead>
						
						<tr>
							<th data-field="id">ID&nbsp;&nbsp;&nbsp;</th>
							<th data-field="ruleName" width="25">RuleName&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</th>
							<th data-field="fixed">Fixed&nbsp;&nbsp;&nbsp;</th>
							 <th data-field="repeats" >Repeats&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</th>
							<th data-field="from">From&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</th>
							<th data-field="to">To&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</th>
							<th data-field="parameter">Parameter&nbsp;&nbsp;&nbsp;
								Value &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Overwrite&nbsp;</th>

						</tr>
					</thead>
			
			<form:form action="/CommissionTool/update" method="post"
						id="ruleListForm" modelAttribute="ruleListContainer">
				<c:choose> 
				<c:when test="${assObj.id != null}">
					<input type="hidden" name="id" value="${assObj.id}">
					<input type="hidden" name="name" value="${assObj.role.roleName}">
				</c:when>
				<c:otherwise>
					<input type="hidden" name="assid" value="${assObj1.id}">
					<input type="hidden" name="empName" value="${assObj1.employee.employeeName}">
				</c:otherwise>	
				</c:choose>	
					
					<tbody id="ruleListContainer">
						<c:forEach items="${List2}" var="as" varStatus="vs">

							<tr class="rule">
								<td><input type="hidden" name="ruleList[${vs.index}].id" value="${as.rule.id}" /> ${as.rule.id}</td>
                        		<td><input type="hidden" name="ruleList[${vs.index}].ruleName" value="${as.rule.ruleName}" /> ${as.rule.ruleName}</td>
								<td>&nbsp;&nbsp;<input type="checkbox" name="ruleList[].fixed" value="fixed" /></td>
								<td>&nbsp;&nbsp; <input type="checkbox" name="ruleList[].repeats" value="repeats">						
  									<select name="ruleList[].frequency" style="width: 80px;"> 
											<option value="">--Select--</option>
										<c:forEach items="${listfrequency}" var="freq">
											<option value="${freq.frequencyName}" />
											<c:out value="${freq.frequencyName}" />
										</c:forEach>
									</select>
								</td>
								<td>&nbsp;<input class="datepicker" type="text" name="ruleList[].Startdate" value="${as.startDate}" size="9" height="0.10"></td>
								<td>&nbsp;<input  class="datepicker" type="text" name="ruleList[].endDate" value="${as.endDate}" size="9" height="0.10"></td>

								<td>
									<table id="customers"  border="1">
										<c:forEach items="${as.ruleAssignmentParameter}" var="asss" varStatus="assignments">
											<tr >
											  <td><input type="hidden" name="ruleList[${vs.index}].ruleAssignmentParameter[${assignments.index}].parameterName" value="${asss.parameterName}" /> ${asss.parameterName}</td>
                                                <td><input type="hidden" value="${asss.overwriteValue}" /> ${asss.overwriteValue}</td>
											
										
											    <td>&nbsp;<input type="text" name="ruleList[${vs.index}].ruleAssignmentParameter[${assignments.index}].overwriteValue" value=""size="7"height="0.01">&nbsp;or&nbsp; 
												<select style="width: 80px;">
																<option value="">--Select--</option>
														<c:forEach items="${targetlist}" var="target">
																 <option value="${target.displayName}"/>
                                 								 <c:out value="${target.displayName}" />
                                                          </c:forEach>
                                                 </select>
                                             </td>
											</tr>
										</c:forEach>
									</table>
								</td>	
							<br>
							</tr>
						</c:forEach><c:choose> 
				<c:when test="${assObj1.id != null || assObj.id != null}"><td colspan="7" align="left"><input type="submit" value="Update"></td></c:when></c:choose></tbody>
			</form:form>
			
			
		</table>
	</div>		
</center>

</tiles:putAttribute>
</tiles:insertDefinition>