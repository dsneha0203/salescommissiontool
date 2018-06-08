<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

<tiles:insertDefinition name="defaultTemplate">
		<tiles:putAttribute name="body">
<title>Sales Commissioning Tool</title>



<link rel="stylesheet" href="../resources/css/jquery-ui.css" />
<link rel="stylesheet" href="../resources/css/style.css" />

<script src="../resources/js/jquery.1.12.1.min.js"></script>
<script src="../resources/js/jquery-ui.js"></script>

<script src="../resources/js/jquery.datePicker-2.1.1.js"></script>

<style>
table {
	width: 750px;
	border: 1px solid #AAA;
	table-layout: auto;
	border-collapse: collapse;
}

table td, table th {
	border: 1px solid #DDD;
	text-align: left;
	padding: 5px 15px 5px 15px;
	-webkit-box-sizing: border-box;
	box-sizing: border-box;
}
</style>



<script type="text/javascript">
	$(function() {
		$('input').filter('.datepicker').datepicker({
			changeMonth : true,
			changeYear : true,
			depth : "year",
			showOn : 'button',
			buttonImage : '../resources/image/calendar1.png',
			buttonImageOnly : true
		});
	});

	</script>
	
	<script>
	
	function rowAdded(rowElement) {
		//clear the imput fields for the row
		$(rowElement).find("input").val('');
		//may want to reset <select> options etc

		//in fact you may want to submit the form
		saveNeeded();
	}
	function rowRemoved1(rowElement) {
		saveNeeded();
	}

	function saveNeeded() {
		$('#submit').css('color', 'red');
		$('#submit').css('font-weight', 'bold');
		if ($('#submit').val().indexOf('!') != 0) {
			$('#submit').val('!' + $('#submit').val());
		}
	}

	function beforeSubmit1() {
		alert('submitting....');
		return true;
	}

	$(document)
			.ready(
					function() {
						var config = {
							rowClass : 'qualiClause',
							addRowId : 'addPerson1',
							removeRowClass : 'removePerson1',
							formId : 'personListForm',
							rowContainerId : 'personListContainer1',
							indexedPropertyName : 'personList',
							indexedPropertyMemberNames : 'fieldName,condition,conditionValue,value',
							rowAddedListener : rowAdded,
							rowRemovedListener : rowRemoved1,
							beforeSubmit : beforeSubmit1
						};
						new DynamicListHelper(config);
					});

	/*-----------------------------------------------------------------------------------------*/

	
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
				$('#submit').css('color', 'red');
				$('#submit').css('font-weight', 'bold');
				if ($('#submit').val().indexOf('!') != 0) {
					$('#submit').val('!' + $('#submit').val());
				}
			}

			function beforeSubmit() {
				alert('submitting....');
				return true;
			}

			$(document)
					.ready(
							function() {
								var config = {
									rowClass : 'beneficiary',
									addRowId : 'addPerson',
									removeRowClass : 'removePerson',
									formId : 'personListForm1',
									rowContainerId : 'personListContainer',
									indexedPropertyName : 'personList1',
									indexedPropertyMemberNames : 'parameterName,parameterValue',
									rowAddedListener : rowAdded,
									rowRemovedListener : rowRemoved,
									beforeSubmit : beforeSubmit
								};
								new DynamicListHelper(config);
							});
	
	
	
	
</script>

<div style="height: 580px; overflow: auto;">
	

			<form:form action="/CommissionTool" method="post" id="personListForm">

				<h1 align="center">Split Rule Details</h1>
				<div align="center">
					<table>
						<tr>
							<td>Rule name:</td>
							<td><input type="text" size=50 value="${splitRuleDetails.splitRuleName}"></input></td>
						</tr>
						<tr>
							<td valign="top">Description:</td>
							<td valign="top"><textarea rows="4" cols="50"
									style="vertical-align: top">${splitRuleDetails.description}</textarea></td>
						</tr>
						<tr>
							<td>Split Plan validity :</td>
							<td>From&nbsp; &nbsp;<input class="datepicker" type="text"
								name="" size="11" height="0.10" value="${splitRuleDetails.startDate}">
								&nbsp;&nbsp;&nbsp;To&nbsp;&nbsp;&nbsp;<input class="datepicker"
								type="text" name="" size="11" height="0.10" value="${splitRuleDetails.endDate}">

							</td>
						</tr>
						<tr>
							<td valign="top">Applies when :</td>
							<td>

								<table>

									<tr>
									<tbody id="personListContainer1">
										<c:forEach items="${qualifyingList}" var="quali">
										<tr class="qualiClause">
											<td>&nbsp;FieldName&nbsp;<select
												name="personList[].fieldName">
													<option value="${quali.fieldList.displayName}">---${quali.fieldList.displayName}---</option>
													<c:forEach items="${listRule2}" var="rule">
														<option value="${rule.displayName}">
															<c:out value="${rule.displayName}" />
														</option>
													</c:forEach>
											</select>&nbsp;
											</td>
											<td>Not&nbsp;<input type="text"
												name="personList[].condition" value="${quali.notFlag}"
												size="2"></td>

											<td>Condition&nbsp;<select
												name="personList[].conditionValue">
													<option value="${quali.conditionList.conditionValue}">---${quali.conditionList.conditionValue}---</option>
													<c:forEach items="${listRule3}" var="rule">
														<option value="${rule.conditionValue}">
															<c:out value="${rule.conditionValue}" />
														</option>
													</c:forEach>
											</select>
											</td>

											<td>&nbsp;Value&nbsp;<input type="text"
												name="personList[].value" value="${quali.value}" /></td>

											<td><a href="#" class="removePerson1">Remove</a></td>
										</tr>
									</c:forEach>

										<c:if test="${personListContainer1.personList.size() == 0}">
											<tr class="qualiClause defaultRow">
												<td>&nbsp;Field Name&nbsp;<select
													name="personList[].fieldName">
														<c:forEach items="${listRule2}" var="rule">
															<option value="${rule.displayName}">
																<c:out value="${rule.displayName}" />
															</option>
														</c:forEach>
												</select></td>

												<td>&nbsp;Not&nbsp;<input type="text"
													name="personList[].condition" value="TRUE" size="2"></td>

												<td>&nbsp;Condition&nbsp;<select
													name="personList[].conditionValue"><c:forEach
															items="${listRule3}" var="rule">
															<option value="${rule.conditionValue}">
																<c:out value="${rule.conditionValue}" />
															</option>
														</c:forEach></select>
												<td>&nbsp;Value&nbsp;<input type="text"
													name="personList[].value"></td>
												<td><a href="#" class="removePerson1">Remove</a></td>

											</tr>
										</c:if>
									</tbody>
								</table> <a href="#" id="addPerson1">Add</a>&nbsp;&nbsp; <a href="?f=">Reset
									List</a>

							</td>
						</tr>
						
						<tr>
						<td></td>
						<td>

							<table id="personListForm1">
								<tbody id="personListContainer">
								<tr><td>Beneficiary</td><td>Split Percentage</td></tr>
								<c:forEach items="${beneficiary}" var="benefi">
								
									<tr class="beneficiary defaultRow">
										<td><select name="">
									<option value="${benefi.beneficiaryType}" selected="selected">-${benefi.beneficiaryType}-</option>
									<option value="">Admin</option>
									<option value="" >Sales Representative</option>
									<option value="">Supporting Engineer</option>
									<option value="">Manager</option>
									<option value="">Second Level Manager</option>
										</select></td>
										
										
										<td><input type="text"
											name="" size="4" value="${benefi.splitPercentage}" /></td>

										<td><a href="#" class="removePerson">Remove</a></td>
									</tr>
								</c:forEach>

								</tbody>
							</table> <a href="#" id="addPerson">Add</a>&nbsp;&nbsp; <a
							href="?f=">Reset List</a>

						</TD>
					</tr>
					</table>
				</div><br/>
				<div align=center>
					<input type="button" value="Update"></input>

				</div>

			</form:form>
</div>
		</tiles:putAttribute>
	</tiles:insertDefinition>
