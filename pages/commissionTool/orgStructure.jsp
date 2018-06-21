 <%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%> 
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>  
 <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
 
 
<tiles:insertDefinition name="defaultTemplate">
    <tiles:putAttribute name="body">


<head>
    <title>OrganizationStructure</title>
</head>

<style>
ul{
list-style-type: none;
}

.wrapper{ 
	overflow-y:auto; 
    position:relative;
    height: 500px;
    padding-top: 30px;
}



</style> 
 <link rel="stylesheet" href="resources/css/kendo.common.min.css" />
    <link rel="stylesheet" href="resources/css/kendo.default.min.css" />

    <script src="resources/js/jquery-1.12.1.min.js"></script>
    <script src="resources/js/kendo.all.min.js"></script>
  
  <div align="center"><h1>Organization Structure </h1></div>
   
   <div class="wrapper">   
<table>

<!-- <ul id="panelbar">-->
<tr width="80%"><td></td><td width="20%"></td>
<tr><td align="left">
				
				<c:set var="count" value="0" scope="page"/>
				<c:set var="bossListCount" value="${fn:length(bossList)}" scope="page"/>
				<!--<c:out value="${bossListCount}"></c:out>-->
				<c:forEach items="${roleList}" var="role">
				<ul>
					<c:if test="${role.reportsTo == null }">
					
					<c:set var="bossListCount" value="${bossListCount - 1}" scope="page"/>
					
					<c:set var="count" value="0"/>
					
					<li>
					<span class="Collapsable"><c:forEach begin="0" end="${count}" step="1">&nbsp;&nbsp;</c:forEach>- &nbsp;<a href='/CommissionTool/roleDetails/${role.id}'>${role.roleName}</a></span><br>
					<!--<c:out value="${bossListCount}"></c:out>-->
					<ul>
					<c:forEach items="${roleList}" var="role2">
					
						<c:if test="${role2.roleName != role.roleName}">
						
							<c:if test="${ role2.reportsTo.roleName == role.roleName}">
							<li>
							<span class="Collapsable"><c:forEach begin="0" end="${count}" step="1">&nbsp;&nbsp;</c:forEach>- &nbsp;<a href='/CommissionTool/roleDetails/${role2.id}'>${role2.roleName}</a></span>
							</li>
							<c:set var="counter" value="0" scope="page"/>	
							<c:forEach items="${bossList}" var="boss">
							
								<c:if test="${counter eq 0 }">
								<ul>
								
								<c:if test="${role2.roleName == boss }">
								<c:set var="counter" value="${counter+1 }" scope="page"/>
								
								<c:set var="bossListCount" value="${bossListCount - 1}" />
								<!--<c:out value="${bossListCount}"></c:out>-->
								<!--<c:out value="counter= ${counter}"></c:out>-->
									<c:forEach items="${roleList}" var="role3">
									
										<c:if test="${role3.roleName != role2.roleName}">
											
										<c:if test="${ role3.reportsTo.roleName == role2.roleName}">
										<li>
											<span class="Collapsable"><c:forEach begin="0" end="${count}" step="1">&nbsp;&nbsp;</c:forEach>- &nbsp;<a href='/CommissionTool/roleDetails/${role3.id}'>${role3.roleName}</a></span>
										
										</li>
										
											<c:if test="${bossListCount>=0}">
											<!--<c:out value="bossListCount= ${bossListCount}"></c:out>-->
											<c:set var="counter2" value="0" scope="page"></c:set>
											<c:forEach items="${bossList}" var="boss">
											<c:if test="${counter2 eq 0 }">
											<c:if test="${role3.roleName == boss }">
											<c:set var="counter2" value="${counter2 + 1 }" scope="page"></c:set>
											
											<ul>
											
												
												<c:forEach var="i" begin="1" end="${bossListCount-1}" step="1">
												
												<c:set var="bossListCount" value="${bossListCount-i}" />
												<c:if test="${bossListCount>=0}">
												<!--<c:out value="${bossListCount}"></c:out><br>
												<c:out value="in loop"></c:out>-->
												<c:set var="loop" value="4" />
												
													<c:forEach items="${roleList}" var="loop">
													
														<c:if test="${loop.roleName != role3.roleName}">
														<c:if test="${ loop.reportsTo.roleName == role3.roleName}">
														 <li>
											<span class="Collapsable"><c:forEach begin="0" end="${count}" step="1">&nbsp;&nbsp;</c:forEach>- &nbsp;<a href='/CommissionTool/roleDetails/${loop.id}'>${loop.roleName}</a></span>
														</li>
														<c:if test="${bossListCount>=0}">
														<c:set var="counter3" value="0"></c:set>
														<c:forEach items="${bossList}" var="boss">
														<c:if test="${counter3 eq 0 }">
														<c:if test="${loop.roleName == boss }">
														<c:set var="bossListCount" value="${bossListCount - 1}" />
														<!--<c:out value="${bossListCount}"></c:out>-->
														<c:set var="counter3" value="${counter3 + 1 }"></c:set>
														<ul>
														<c:forEach items="${roleList}" var="inner_loop">
														<c:if test="${inner_loop.roleName != loop.roleName}">
														<c:if test="${inner_loop.reportsTo.roleName == loop.roleName}">
														 <li>
											<span class="Collapsable"><c:forEach begin="0" end="${count}" step="1">&nbsp;&nbsp;</c:forEach>- &nbsp;<a href='/CommissionTool/roleDetails/${inner_loop.id}'>${inner_loop.roleName}</a></span>
														</li>
														
															<c:if test="${bossListCount>=0}">
														<c:set var="counter4" value="0"></c:set>
														<c:forEach items="${bossList}" var="boss">
														<c:if test="${counter4 eq 0 }">
														<c:if test="${inner_loop.roleName == boss }">
														<c:set var="bossListCount" value="${bossListCount - 1}" />
														<!--<c:out value="${bossListCount}"></c:out>-->
														<c:set var="counter4" value="${counter4 + 1 }"></c:set>
														<ul>
														<c:forEach items="${roleList}" var="inner_loop_1">
														<c:if test="${inner_loop_1.roleName != inner_loop.roleName}">
														<c:if test="${inner_loop_1.reportsTo.roleName == inner_loop.roleName}">
														 <li>
											<span class="Collapsable"><c:forEach begin="0" end="${count}" step="1">&nbsp;&nbsp;</c:forEach>- &nbsp;<a href='/CommissionTool/roleDetails/${inner_loop_1.id}'>${inner_loop_1.roleName}</a></span>
														</li>
														
															<c:if test="${bossListCount>=0}">
														<c:set var="counter5" value="0"></c:set>
														<c:forEach items="${bossList}" var="boss">
														<c:if test="${counter5 eq 0 }">
														<c:if test="${inner_loop_1.roleName == boss }">
														<c:set var="bossListCount" value="${bossListCount - 1}" />
														<!--<c:out value="${bossListCount}"></c:out>-->
														<c:set var="counter5" value="${counter5 + 1 }"></c:set>
														<ul>
														<c:forEach items="${roleList}" var="inner_loop_2">
														<c:if test="${inner_loop_2.roleName != inner_loop_1.roleName}">
														<c:if test="${inner_loop_2.reportsTo.roleName == inner_loop_1.roleName}">
														 <li>
											<span class="Collapsable"><c:forEach begin="0" end="${count}" step="1">&nbsp;&nbsp;</c:forEach>- &nbsp;<a href='/CommissionTool/roleDetails/${inner_loop_2.id}'>${inner_loop_2.roleName}</a></span>
														</li>
														
														
															<c:if test="${bossListCount>=0}">
														<c:set var="counter6" value="0"></c:set>
														<c:forEach items="${bossList}" var="boss">
														<c:if test="${counter6 eq 0 }">
														<c:if test="${inner_loop_2.roleName == boss }">
														<c:set var="bossListCount" value="${bossListCount - 1}" />
														<!--<c:out value="${bossListCount}"></c:out>-->
														<c:set var="counter6" value="${counter6 + 1 }"></c:set>
														<ul>
														<c:forEach items="${roleList}" var="inner_loop_3">
														<c:if test="${inner_loop_3.roleName != inner_loop_2.roleName}">
														<c:if test="${inner_loop_3.reportsTo.roleName == inner_loop_2.roleName}">
														 <li>
											<span class="Collapsable"><c:forEach begin="0" end="${count}" step="1">&nbsp;&nbsp;</c:forEach>- &nbsp;<a href='/CommissionTool/roleDetails/${inner_loop_3.id}'>${inner_loop_3.roleName}</a></span>
														</li>
														
														<c:if test="${bossListCount>=0}">
														<c:set var="counter7" value="0"></c:set>
														<c:forEach items="${bossList}" var="boss">
														<c:if test="${counter7 eq 0 }">
														<c:if test="${inner_loop_3.roleName == boss }">
														<c:set var="bossListCount" value="${bossListCount - 1}" />
														<!--<c:out value="${bossListCount}"></c:out>-->
														<c:set var="counter7" value="${counter7 + 1 }"></c:set>
														<ul>
														<c:forEach items="${roleList}" var="inner_loop_4">
														<c:if test="${inner_loop_4.roleName != inner_loop_3.roleName}">
														<c:if test="${inner_loop_4.reportsTo.roleName == inner_loop_3.roleName}">
														 <li>
											<span class="Collapsable"><c:forEach begin="0" end="${count}" step="1">&nbsp;&nbsp;</c:forEach>- &nbsp;<a href='/CommissionTool/roleDetails/${inner_loop_4.id}'>${inner_loop_4.roleName}</a></span>
														</li>
														
														<c:if test="${bossListCount>=0}">
														<c:set var="counter8" value="0"></c:set>
														<c:forEach items="${bossList}" var="boss">
														<c:if test="${counter8 eq 0 }">
														<c:if test="${inner_loop_4.roleName == boss }">
														<c:set var="bossListCount" value="${bossListCount - 1}" />
														<!--<c:out value="${bossListCount}"></c:out>-->
														<c:set var="counter8" value="${counter8 + 1 }"></c:set>
														<ul>
														<c:forEach items="${roleList}" var="inner_loop_5">
														<c:if test="${inner_loop_5.roleName != inner_loop_4.roleName}">
														<c:if test="${inner_loop_5.reportsTo.roleName == inner_loop_4.roleName}">
														 <li>
											<span class="Collapsable"><c:forEach begin="0" end="${count}" step="1">&nbsp;&nbsp;</c:forEach>- &nbsp;<a href='/CommissionTool/roleDetails/${inner_loop_5.id}'>${inner_loop_5.roleName}</a></span>
														</li>
														
														<c:if test="${bossListCount>=0}">
														<c:set var="counter9" value="0"></c:set>
														<c:forEach items="${bossList}" var="boss">
														<c:if test="${counter9 eq 0 }">
														<c:if test="${inner_loop_5.roleName == boss }">
														<c:set var="bossListCount" value="${bossListCount - 1}" />
														<!--<c:out value="${bossListCount}"></c:out>-->
														<c:set var="counter9" value="${counter9 + 1 }"></c:set>
														<ul>
														<c:forEach items="${roleList}" var="inner_loop_6">
														<c:if test="${inner_loop_6.roleName != inner_loop_5.roleName}">
														<c:if test="${inner_loop_6.reportsTo.roleName == inner_loop_5.roleName}">
														 <li>
											<span class="Collapsable"><c:forEach begin="0" end="${count}" step="1">&nbsp;&nbsp;</c:forEach>- &nbsp;<a href='/CommissionTool/roleDetails/${inner_loop_6.id}'>${inner_loop_6.roleName}</a></span>
														</li>
														
														<c:if test="${bossListCount>=0}">
														<c:set var="counter10" value="0"></c:set>
														<c:forEach items="${bossList}" var="boss">
														<c:if test="${counter10 eq 0 }">
														<c:if test="${inner_loop_6.roleName == boss }">
														<c:set var="bossListCount" value="${bossListCount - 1}" />
														<!--<c:out value="${bossListCount}"></c:out>-->
														<c:set var="counter10" value="${counter10 + 1 }"></c:set>
														<ul>
														<c:forEach items="${roleList}" var="inner_loop_7">
														<c:if test="${inner_loop_7.roleName != inner_loop_6.roleName}">
														<c:if test="${inner_loop_7.reportsTo.roleName == inner_loop_6.roleName}">
														 <li>
											<span class="Collapsable"><c:forEach begin="0" end="${count}" step="1">&nbsp;&nbsp;</c:forEach>- &nbsp;<a href='/CommissionTool/roleDetails/${inner_loop_7.id}'>${inner_loop_7.roleName}</a></span>
														</li>
														</c:if>														
														</c:if>
														</c:forEach>													
														</ul>
														</c:if>
														</c:if>
														</c:forEach>
														</c:if>
														
														</c:if>														
														</c:if>
														</c:forEach>													
														</ul>
														</c:if>
														</c:if>
														</c:forEach>
														</c:if>
														
														
														</c:if>														
														</c:if>
														</c:forEach>													
														</ul>
														</c:if>
														</c:if>
														</c:forEach>
														</c:if>
														
														
														</c:if>														
														</c:if>
														</c:forEach>													
														</ul>
														</c:if>
														</c:if>
														</c:forEach>
														</c:if>
														
														</c:if>														
														</c:if>
														</c:forEach>													
														</ul>
														</c:if>
														</c:if>
														</c:forEach>
														</c:if>
														
														
														</c:if>														
														</c:if>
														</c:forEach>													
														</ul>
														</c:if>
														</c:if>
														</c:forEach>
														</c:if>
														
														
														
														</c:if>														
														</c:if>
														</c:forEach>													
														</ul>
														</c:if>
														</c:if>
														</c:forEach>
														</c:if>
														
														
														
														
														</c:if>														
														</c:if>
														</c:forEach>													
														</ul>
														</c:if>
														</c:if>
														</c:forEach>
														</c:if>
														</c:if>
														</c:if>
														
													</c:forEach>
												
												</c:if>
												</c:forEach>
											</ul>
											
											</c:if>	
											</c:if>
											</c:forEach>
											</c:if>
										</c:if>
										
										</c:if>
																		
									</c:forEach>
								
								</c:if>
								</ul>
								</c:if>
							</c:forEach>
							</c:if>		
										
						</c:if>
					
					</c:forEach>
					</ul>
					
				<c:set var="count" value="${count + 1}" scope="page"/>
				</li>
				
					</c:if>
					
					
					
				</ul>
				</c:forEach>
								
			
</td>
    <td>&nbsp;</td>
    </tr>
</table>

</div>


 <script>
	 $("#panelbar").kendoPanelBar({
			scorallable : true,
			animation : {
				expand : {
					duration : 1000
				}

			}

		});
		
	    
	    $(".Collapsable").click(function () {
	        $(this).parent().children().toggle();
	        $(this).toggle();
	        $(this).html($(this).html().startsWith('+') ? $(this).html().replace('+','-') : $(this).html().replace('-','+'));
	    });
			
</script>
	

           


 
 </tiles:putAttribute>
 </tiles:insertDefinition>