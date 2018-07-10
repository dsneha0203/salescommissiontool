<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

<tiles:insertDefinition name="defaultTemplate">
<tiles:putAttribute name="body">
<head>
	<title>CompPlan</title>
	<link rel="stylesheet" href="resources/css/jquery-ui.css" />
	<link rel="stylesheet" href="resources/css/style.css" />

	<script src="resources/js/jquery.1.12.1.min.js"></script>
	<script src="resources/js/jquery-ui.js"></script>
</head>

<style>
#header {
    background-color:#c7cfd1;
    color:black;
    text-align:center;
    font-family: "Trebuchet MS", Times New Roman, Helvetica, sans-serif;
    padding:5px;
}
body {
    background-color: #ffffff;
}
#customers {
    font-family: "Trebuchet MS", Times New Roman, Helvetica, sans-serif;
    border-collapse: collapse;
    width: 100%;
}
#customers td, #customers th {
    border: 1px solid #ddd;
    text-align: left;
    padding: 8px;
}
#customers tr:nth-child(even){background-color: #f2f2f2}
#customers tr:hover {background-color: #ddd;}
#customers th {
    padding-top: 12px;
    padding-bottom: 12px;
    background-color: #c7cfd1;
    color: black;
}
#check_role{
display:none;
}
#check_emp{
display:none;
}
html {
overflow: -moz-scrollbars-vertical; 
overflow-y: scroll;
}
</style>

<script>
/*----------------------------Script for window popup -------------------------- */ 
    function openWindow(){
    	window.open('/CommissionTool/search' , '' , 'width=1400,height=700,scrollbars=yes');
    	}
 
/*----------------------------Script for Radio button------------------------------ */ 
 $(document).ready(function(){
            $('input[type=radio][name=test1]').click(function(){
                var related_class=$(this).val();
                $('.'+related_class).prop('disabled',false);
                
                $('input[type=radio][name=test1]').not(':checked').each(function(){
                    var other_class=$(this).val();
                    $('.'+other_class).prop('disabled',true);
                });
            });
            
            $("#emp_rad").click(function(){
            	document.getElementById("role_op").value="";
            	$("#check_emp").show(500);
            	 $("#check_role").hide(500);
            	
             });
             
             $("#role_rad").click(function(){
            	 document.getElementById("text1").value="";
            	  $("#check_role").show(500);
            	 $("#check_emp").hide(500);
            	 
             });
 });
 
       
 $('input[type="radio"]').on('click', function() {
	    var val = $(this).val(),
	        selectId = $(this).closest('tr').find('select').first();
	    
	    if ($(this).val() === 'true') {
	        $(selectId).prop("disabled", false);
	      
	    }
	    else {
	        $(selectId).prop("disabled", true);
	       
	    }
	});
 
 
 /*------------------------------------Script for Datepicker------------------------------- */ 
  $(function() {
    	   $('input').filter('.datepicker').datepicker({
    	      changeMonth: true,     
    	      changeYear: true,
    	      depth: "year",
    	     // dateFormat: 'dd/mm/yy',
    	      showOn: 'button',      
    	      buttonImage: 'resources/image/c.png',
    	      buttonImageOnly: true,
    	     });
 });
    
 /*------------------------------------Script for Rulelist Container2------------------------------ */  
 function rowAdded(rowElement) {
	    //clear the imput fields for the row
	    $(rowElement).find("input").val('');
	    //may want to reset <select> options etc
	    //in fact you may want to submit the form
	    saveNeeded();
	}
	function rowRemoved(rowElement) {
	    saveNeeded();
	}
	function saveNeeded() {
	    $('#submit').css('color','red');
	    $('#submit').css('font-weight','bold');
	    if( $('#submit').val().indexOf('!') != 0 ) {
	        $('#submit').val( '!' + $('#submit').val() );
	    }
	}
	function beforeSubmit() {
	    alert('wait your table is updating....!!');
	    return true;
	}
	$(document).ready( function() {
	    var config = {
	        rowClass : 'rule',
	        addRowId : 'addRule',
	        removeRowClass : 'removeRule',
	        formId : 'ruleListForm',
	        rowContainerId : 'ruleListContainer',
	        indexedPropertyName : 'ruleList',
	        indexedPropertyMemberNames : 'id,ruleName,fixed,repeats,frequency,Startdate,endDate,ruleAssignmentParameter.parameterName,ruleAssignmentParameter.overwriteValue',
	        rowAddedListener : rowAdded,
	        rowRemovedListener : rowRemoved,
	        beforeSubmit : beforeSubmit
	    };
	    new DynamicListHelper(config);
	    
	    var emp_input_val = document.getElementById("text1").value;
	    if(emp_input_val != ""){
	    	document.getElementById("role_op").value="";
	    	document.getElementById("role_rad").checked=false;
	    	document.getElementById("emp_rad").checked=true;
	    	document.getElementById("mag_emp").disabled=false;
	    	$("#check_emp").show(500);
	    	
	    }
	    
	    var role_input_val = document.getElementById("role_op").value;
	    if(role_input_val != ""){
	    	document.getElementById("text1").value="";
	    	document.getElementById("emp_rad").checked=false;
	    	document.getElementById("role_rad").checked=true;
	    	document.getElementById("role_op").disabled=false;
	    	 $("#check_role").show(500);
	    	
	    }
	});
	
	
	
	/*------------------------------------Script for Rulelist Container2------------------------------ */  
	
	function rowAdded(rowElement) {
	    //clear the imput fields for the row
	    $(rowElement).find("input").val('');
	    //may want to reset <select> options etc
	    //in fact you may want to submit the form
	    saveNeeded();
	}
	function rowRemoved(rowElement) {
	    saveNeeded();
	}
	function saveNeeded() {
	    $('#submit').css('color','red');
	    $('#submit').css('font-weight','bold');
	    if( $('#submit').val().indexOf('!') != 0 ) {
	        $('#submit').val( '!' + $('#submit').val() );
	    }
	}
	function beforeSubmit() {
	    alert('wait....!!');
	    return true;
	}
	
	
	function setEmpValue(value){
		
		 document.getElementById("text2").value= value;
	}
	function setRoleValue(value){
		
		document.getElementById("text3").value=value;
	}
	$(document).ready( function() {
	    var config = {
	        rowClass : 'rule1',
	        addRowId : 'addRule1',
	        removeRowClass : 'removeRule1',
	        formId : 'ruleListForm1',
	        rowContainerId : 'ruleListContainer1',
	        indexedPropertyName : 'ruleList1',
	        indexedPropertyMemberNames : 'id,ruleName,fixed,repeats,frequency,Startdate,endDate,ruleAssignmentParameter.parameterName,ruleAssignmentParameter.overwriteValue',
	        rowAddedListener : rowAdded,
	        rowRemovedListener : rowRemoved,
	        beforeSubmit : beforeSubmit
	    };
	    new DynamicListHelper(config);
	    
	   
	    
	});
 
</script>


		<br><br>
		<h2 align="center">Compensation Plan Assignment</h2>
		<!-- ------------------------------------------This part is for choosing employee or role----------------------------------- -->
		<div id="header">
		
			
				 <table>
				 <tr>
				 <td>
				 <label for="radio1"> 
						<input type="radio" name="test1" value="radio1" id="emp_rad"/><b>Employee</b>
						</label>
				 </td>
				 <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
				 <td>
				
				 <label for="radio2"><input type="radio" name="test1"
									value="radio2" id="role_rad"/>Role </label>
				 </td>
				 </tr>
					<tr>
						<td>
						<!--  <label for="radio1"> 
						<input type="radio" name="test1" value="radio1" /><b>Employee</b>
						</label><br>-->
						 Select Employee :&nbsp;&nbsp; <input type="image" img
							src="resources/image/search.png"
							style="height: 30px; width: 30px;" class="radio1"
							disabled onclick="openWindow()" id="mag_emp"/>
							&nbsp;&nbsp;&nbsp;&nbsp;
						</td>
						<td>
							<form id="form1" action="/CommissionTool/searchemp" method="post">
								Selected Employee: <input type="text" name="EmployeeName"
									id="text1" name="for_radio1[]" class="radio1" disabled
									size="11" required value="${empNameChosen }" oninput="setEmpValue(this.value)"/>
									<!--<c:forEach items="${empList}"
													var="emp">value="${emp.employeeName }"</c:forEach> />-->
								<input type="submit" value="check" id="check_emp" />
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>Or</b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							</form>
						</td>
						<td>
							<form action="/CommissionTool/Submitrole" method="post" model="command">
								<!-- <label for="radio2"><input type="radio" name="test1"
									value="radio2" />Role </label><br> -->
									 Select Role :<select name="roleName" 
									 class="radio2" name="for_radio2[]" id="role_op"
									disabled required onchange="setRoleValue(this.value)">
									<c:choose>
									<c:when test="${rlelist.roleName != null }">
									 <option value="${rlelist.roleName }">${rlelist.roleName}</option> 
									 <c:forEach items="${listRole}" var="role">
										<option value="${role.roleName}">
											<c:out value="${role.roleName}" />
										</option>
									</c:forEach>
									</c:when>
									<c:otherwise>
									<option value="">---Choose Role---</option> 
									<c:forEach items="${listRole}" var="role">
										<option value="${role.roleName}">
											<c:out value="${role.roleName}" />
										</option>
									</c:forEach>
									</c:otherwise>
									</c:choose>
								</select> <input type="submit" value="check" id="check_role" >
							</form>
						</td>
						
					</tr>
					
				</table>
			
		
		</div>

		<!-- ------------------------------------------This part is for displaying table against Employee/Role----------------------------------- -->
		
			
				
				
				<table id="customers" border="1">
					<thead>
						<h3 align="center">Selected Rule</h3>
						<tr>
							<th data-field="id">ID&nbsp;&nbsp;&nbsp;</th>
							<th data-field="ruleName" width="25">RuleName&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</th>
							<th data-field="fixed">Fixed&nbsp;&nbsp;&nbsp;</th>
							 <th data-field="repeats" >Repeats&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</th>
							<th data-field="from">From&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</th>
							<th data-field="to">To&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</th>
							<th data-field="parameter">Parameter&nbsp;&nbsp;&nbsp;
								Value &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Overwrite&nbsp;</th>
							<th></th>
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
                        		
								<td>&nbsp;&nbsp;<input type="checkbox" name="ruleList[].fixed" id="fixed_checkbox" value="fixed"
								<c:if test="${as.validityType eq 'fixed' }">
								checked
								</c:if> ></td>
								<td>&nbsp;&nbsp; <input type="checkbox" name="ruleList[].repeats" value="repeats" id="repeats_checkbox" 
								<c:if test="${as.validityType eq 'repeats'}">
								checked
								</c:if>
								>						
  									<select name="ruleList[].frequency" > 
											<option value="${as.frequency.frequencyName }">--${as.frequency.frequencyName }--</option>
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
											
										
											    <td>&nbsp;<input type="number" name="ruleList[${vs.index}].ruleAssignmentParameter[${assignments.index}].overwriteValue" value=""size="7"height="0.01">&nbsp;or&nbsp; 
												<select >
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
								<td><a href="#" class="removeRule">Remove</a></td>
							
							</tr>
						</c:forEach></tbody></table>	
				<c:choose> 
				<c:when test="${assObj1.id != null || assObj.id != null}"><input type="submit" value="Update"></c:when>
				</c:choose>
			</form:form>
					
		<!-- ------------------------------------------This part for creating table against RuleId----------------------------------- -->	
							
				<!--<form:form action="/CommissionTool/create" method="post"
						id="ruleListForm1" modelAttribute="ruleListContainer1">
						
					
						<input type="hidden" name="EmpName" id="text2" > 						
						<input type="hidden" name="RoleName" id="text3">
						
					 	<tbody id="ruleListContainer1">
						<c:forEach items="${ruleList}" var="rs" varStatus="vs">
							<tr class="rule1">
								<td><input type="hidden" name="ruleList1[${vs.index}].id" value="${rs.id}" /> ${rs.id}</td>
                        		<td><input type="hidden" name="ruleList1[${vs.index}].ruleName" value="${rs.ruleName}" /> ${rs.ruleName}</td>
								<td>&nbsp;&nbsp;<input type="checkbox" name="ruleList1[].fixed" value="fixed" /></td>
								<td>&nbsp;&nbsp; <input type="checkbox" name="ruleList1[].repeats" value="repeats">						
  									<select name="ruleList1[].frequency" > 
										<option value="">--Select--</option>
										<c:forEach items="${listfrequency}" var="fre">
											<option value="${fre.frequencyName}" />
											<c:out value="${fre.frequencyName}" />
										</c:forEach>
									</select>
								</td>
								<td>&nbsp;<input class="datepicker" type="text" name="ruleList1[].Startdate"  size="9" height="0.10"></td>
								<td>&nbsp;<input  class="datepicker" type="text" name="ruleList1[].endDate"  size="9" height="0.10"></td>	
								<td>
									<table id="customers"  border="1">
										<c:forEach items="${rs.ruleParameter}" var="ass" varStatus="assignments">
											<tr >
											  <td><input type="hidden" name="ruleList1[${vs.index}].ruleAssignmentParameter[${assignments.index}].parameterName" value="${ass.parameterName}" /> ${ass.parameterName}</td>
											  <td><input type="hidden" name="ruleList1[${vs.index}].ruleAssignmentParameter[${assignments.index}].overwriteValue" value="${ass.parameterValue}" /> ${ass.parameterValue}</td>
											  <td>&nbsp;<input type="number" value=""size="7"height="0.01">&nbsp;or&nbsp; 
											  		<select >
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
							<td><a href="#" class="removeRule1">Remove</a></td>
							
						</tr>
						</c:forEach>
					  </tbody>  
					  <td><input type="submit" value="Create"></td>   
					  </form:form> 
						</table>  
						
			<div>
		<form action="/CommissionTool/submitrule" method="post" model="command">
				 <table>
					<tr>
					<td align="right">
						<select name="Id" required>
							<option value="">--Select--</option>
							<c:forEach items="${listRules}" var="rule">
                                <option>
                                    <c:out value="${rule.id}" />
                                </option>
               			 	</c:forEach>
           	 			</select></td>
           	 			<td>
           			 <input type="submit" value ="add"></td>
            </tr></table></form>
            </div>-->
	</tiles:putAttribute>
</tiles:insertDefinition>