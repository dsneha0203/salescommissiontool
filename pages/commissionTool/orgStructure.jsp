 <%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%> 
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>  

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
		<ul>		
				<c:set var="count" value="0" scope="page"/>
				<c:forEach items="${roleList}" var="role">
				
					<c:if test="${role.reportsTo == null }">
					<li>
					<span class="Collapsable"><c:forEach begin="0" end="${count}" step="1">&nbsp;&nbsp;</c:forEach>- &nbsp;<a href='/CommissionTool/roleDetails/${role.id}'>${role.roleName}</a></span><br>
					
					</c:if>
					
					
					<c:forEach items="${roleList}" var="role2">
					
						<c:if test="${role2.roleName != role.roleName}">
						
							<c:if test="${ role2.reportsTo.roleName == role.roleName}">
						<ul>
						<c:set var="count" value="${count + 1}" scope="page"/>
							<li>
							<span class="Collapsable"><c:forEach begin="0" end="${count}" step="1">&nbsp;&nbsp;</c:forEach>- &nbsp;<a href='/CommissionTool/roleDetails/${role2.id}'>${role2.roleName}</a></span><br>
							</li>
							<c:set var="count" value="${count - 1}" scope="page"/>
						</ul>		
							</c:if>		
										
						</c:if>
					
					</c:forEach>
					
					
				<c:set var="count" value="${count + 1}" scope="page"/>
				
				</c:forEach>
				</li>				
			</ul>
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