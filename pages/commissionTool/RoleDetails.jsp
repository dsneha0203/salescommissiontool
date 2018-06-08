<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<tiles:insertDefinition name="defaultTemplate">
	<tiles:putAttribute name="body">


		<title>RoleDetails</title>



		<style>
table {
	width: 700px;
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

/*-------------------------*/

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
									rowClass : 'person',
									addRowId : 'addPerson1',
									removeRowClass : 'removePerson1',
									formId : 'targetListForm',
									rowContainerId : 'targetListContainer',
									indexedPropertyName : 'targetList',
									indexedPropertyMemberNames : 'targetName,startDate,terminationDate,value',
									rowAddedListener : rowAdded,
									rowRemovedListener : rowRemoved1,
									beforeSubmit : beforeSubmit1
								};
								new DynamicListHelper(config);
							});





/*-------------------------*/




</script>


		<h1 align="center">Role Details</h1>

		<form:form action="/CommissionTool/submitRoleDetails"
			modelAttribute="targetListContainer" method="post"
			id="targetListForm">
			<div align="center">
				<table>
					<tr>
						<td><b>Role Name:</b></td>
						<td><input type="text" name="roleName"
							value="${roleDetails.roleName}"></td>
					</tr>
					<tr>
						<td><b>Description:</b></td>
						<td><textarea rows="4" cols="50" name="description">${roleDetails.description}</textarea></td>
					</tr>
					<tr>
						<td><b>Reports To:</b></td>
						<td>${roleDetails.reportsTo.roleName}</td>
					</tr>
					<tr>
						<td colspan="2">

							<table>
								<tr>
									<td><h3>Set Targets</h3>&nbsp;&nbsp;</td>
									<td>View&nbsp;&nbsp;<a href="#">All</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a
										href="#">Current</a></td>
								</tr>
							</table>
							<table>
								<thead>
									<tr>
										<td>Target Name</td>
										<td>Start Date</td>
										<td>End Date</td>
										<td>value</td>
									</tr>
								</thead>
								<tbody id="targetListContainer">
								
									<c:forEach items="${targetDetails}" var="target">

										<tr class="person">
										<td><select name="targetList[].targetName">
					<option value="${target.targetDefinition.displayName}">---${target.targetDefinition.displayName}---</option>
										
												<c:forEach items="${targetDefinition}" var="tar">
													<option value="${tar.displayName}">
														<c:out value="${tar.displayName}" />
													</option>
												</c:forEach>
										</select></td>

										<td><input type="text" name="targetList[].startDate"
											value="${target.startDate}" size="8"></td>

										<td><input type="text"
											name="targetList[].terminationDate" value="${target.terminationDate}" size="8"></td>
											<td><input type="text"
											name="targetList[].value" value="${target.value}" size="10"></td>
										

											<td><a href="#" class="removePerson">&nbsp;Remove</a></td>
										</tr>
									</c:forEach>

									<tr class="person defaultRow">
										<td><select name="targetList[].targetName">
												<c:forEach items="${targetDefinition}" var="tar">
													<option value="${tar.displayName}">
														<c:out value="${tar.displayName}" />
													</option>
												</c:forEach>
										</select></td>

										<td><input type="text" name="targetList[].startDate"
											value="" size="8"></td>

										<td><input type="text"
											name="targetList[].terminationDate" value="" size="8"></td>
											<td><input type="text"
											name="targetList[].value" value="0" size="10"></td>
										<td><a href="#" class="removePerson1">Remove</a></td>

									</tr>

								</tbody>

							</table> <a href="#" id="addPerson1">Add</a>&nbsp;&nbsp; <a href="?f=">Reset
								List</a>
						
						</td>
					</tr>
				</table>
				<br /> <input type="submit" value="Update">
			</div>
			
		</form:form>

	</tiles:putAttribute>
</tiles:insertDefinition>