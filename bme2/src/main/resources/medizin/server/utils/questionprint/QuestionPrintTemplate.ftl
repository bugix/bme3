<!DOCTYPE html
  PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<#list questionPojoList as questionPojo>
	<div <#if questionPojo_has_next>style="page-break-after: always"</#if>>
	   <p><h2>${questionPojo.question.questionShortName}</h2>
	   <hr /></p>
	   <p>${questionPojo.questionText}</p>
	   	   
	   <#if questionLabelPojo.questionDetailFlag>	   	   
		   <p><b>${questionLabelPojo.questionTypeText} : </b>${questionPojo.question.questionType.longName}</p>
		   <p><b>${questionLabelPojo.author} : </b>${questionPojo.question.autor.name} ${questionPojo.question.autor.prename}<br /></p>
		   <p><b>${questionLabelPojo.reviewer} : </b>${questionPojo.question.rewiewer.name} ${questionPojo.question.rewiewer.prename}<br /></p>
		   <p><b>${questionLabelPojo.questionEvent} : </b>${questionPojo.question.questEvent.eventName}<br /></p>
		   <p><b>${questionLabelPojo.comment} : </b>${questionPojo.question.comment}<br /></p>
		   <p><b>${questionLabelPojo.mcs} : </b>${questionPojo.mcs}<br /></p>
		   <p><b>${questionLabelPojo.createdDate} : </b>${questionPojo.question.dateAdded?string("dd.MM.yyyy")}<br /></p>
		   <p><b>${questionLabelPojo.modifiedDate} : </b><#if questionPojo.question.dateChanged??>${questionPojo.question.dateChanged?string("dd.MM.yyyy")}</#if><br /></p>	   
		   
		   <#if questionPojo.question.questionResources?has_content>
		   		<p style="background-color: #C7C7C7; size:16; padding:6px"><b>${questionLabelPojo.media}</b></p>
		   		<#list questionPojo.question.questionResources as questionResource>
		   			 <p><img class="questionMedia" src="${questionLabelPojo.imageDirPath}${questionResource.path}" alt="File does not exist" /><br /></p>	   			
		   		</#list>
		   </#if>
	   </#if>
	   	   
	   <#if questionLabelPojo.keywordFlag>
	   		<#if questionPojo.question.keywords?has_content>
	   			<p style="background-color: #C7C7C7; size:16; padding:6px"><b>${questionLabelPojo.keywords}</b></p>
	   			<p>
	   				<#list questionPojo.question.keywords as keyword>
	   					${keyword.name} <#if keyword_has_next>, </#if>
	   				</#list>
	   			</p>
	   		</#if>
	   </#if>
	   
	   <#if questionLabelPojo.learningObjectiveFlag>
	   		 <#if questionPojo.question.mainQuestionSkills?has_content || questionPojo.question.minorQuestionSkills?has_content>
		   		<p style="background-color: #C7C7C7; size:16; padding:6px"><b>${questionLabelPojo.learningObjective}</b></p>
		   		
		   		<#if questionPojo.question.mainQuestionSkills?has_content>
		   			<p><b><u>${questionLabelPojo.majorSkill}</u></b><br /></p>
		   			
		   			<table width="100%" style="border:1px solid #CCCCCC; border-collapse:collapse;">
					    <tr style="border:1px solid #CCCCCC;">
						     <th style="border:1px solid #CCCCCC; padding: 5px;">${questionLabelPojo.mainClassification}</th>
						     <th style="border:1px solid #CCCCCC; padding: 5px;">${questionLabelPojo.classificationTopic}</th>
						     <th style="border:1px solid #CCCCCC; padding: 5px;">${questionLabelPojo.skills}</th>
						     <th style="border:1px solid #CCCCCC; padding: 5px;">${questionLabelPojo.skillLevel}</th>
					    </tr>
					    
					    <#list questionPojo.question.mainQuestionSkills as mainSkill>
					    	<tr style="border:1px solid #CCCCCC;">
					    		<td style="border:1px solid #CCCCCC; padding: 5px;">${mainSkill.skill.topic.classificationTopic.mainClassification.shortcut}</td>
					    		<td style="border:1px solid #CCCCCC; padding: 5px;">${mainSkill.skill.topic.classificationTopic.shortcut}</td>
					    		<td style="border:1px solid #CCCCCC; padding: 5px;">${mainSkill.skill.topic.topicDesc}</td>
					    		<td style="border:1px solid #CCCCCC; padding: 5px;"><#if mainSkill.skill.skillLevel??>${mainSkill.skill.skillLevel.levelNumber}</#if></td>
					    	</tr>
					    </#list>
					</table>
		   		</#if>	
		   		
		   		<#if questionPojo.question.minorQuestionSkills?has_content>
		   			<p><b><u>${questionLabelPojo.minorSkill}</u></b><br /></p>
		   			
		   			<table width="100%" style="border:1px solid #CCCCCC; border-collapse:collapse;">
					    <tr style="border:1px solid #CCCCCC;">
						     <th style="border:1px solid #CCCCCC; padding: 5px;">${questionLabelPojo.mainClassification}</th>
						     <th style="border:1px solid #CCCCCC; padding: 5px;">${questionLabelPojo.classificationTopic}</th>
						     <th style="border:1px solid #CCCCCC; padding: 5px;">${questionLabelPojo.skills}</th>
						     <th style="border:1px solid #CCCCCC; padding: 5px;">${questionLabelPojo.skillLevel}</th>
					    </tr>
					    
					    <#list questionPojo.question.minorQuestionSkills as minorSkill>
					    	<tr style="border:1px solid #CCCCCC;">
					    		<td style="border:1px solid #CCCCCC; padding: 5px;">${minorSkill.skill.topic.classificationTopic.mainClassification.shortcut}</td>
					    		<td style="border:1px solid #CCCCCC; padding: 5px;">${minorSkill.skill.topic.classificationTopic.shortcut}</td>
					    		<td style="border:1px solid #CCCCCC; padding: 5px;">${minorSkill.skill.topic.topicDesc}</td>
					    		<td style="border:1px solid #CCCCCC; padding: 5px;"><#if minorSkill.skill.skillLevel??>${minorSkill.skill.skillLevel.levelNumber}</#if></td>
					    	</tr>
					    </#list>
					</table>
		   		</#if>   		
		   </#if>
	   </#if>
	   
	  <#if questionLabelPojo.usedInMcFlag>
	  	<#if questionPojo.assessmentQueList?has_content>
	   		<p style="background-color: #C7C7C7; size:16; padding:6px"><b>${questionLabelPojo.usedInMc}</b></p>
	   		
	   		<table width="100%">
			    <tr>
				     <th>${questionLabelPojo.assessmentName}</th>
				     <th>${questionLabelPojo.mc}</th>
				     <th>${questionLabelPojo.dateOfAssessment}</th>
				     <th>${questionLabelPojo.schwierigkeit}</th>
				     <th>${questionLabelPojo.trenschaerfe}</th>
			    </tr>
			    
			    <#list questionPojo.assessmentQueList as assQue>
			    	<#if assQue??>
			    		<tr>
			    			<td>${assQue.assesment.name}</td>
			    			<td>${assQue.assesment.mc.mcName}</td>
			    			<td>${assQue.assesment.dateOfAssesment?string("dd.MM.yyyy")}</td>
			    			<td>${assQue.schwierigkeit!""}</td>
			    			<td>${assQue.trenschaerfe!""}</td>
			    		</tr>
			    	</#if>		    	
			    </#list>
			</table>
	 	  </#if>
	  </#if>
	   
	  <#if questionLabelPojo.answerFlag>
	  	<#if questionPojo.answerList?has_content || questionPojo.matrixAnswerList?has_content>
	   		<p style="background-color: #C7C7C7; size:16; padding:6px"><b>${questionLabelPojo.answer}</b></p>
	   		
	   		<#if questionPojo.question.questionType.questionType == "Textual" || questionPojo.question.questionType.questionType == "LongText" || questionPojo.question.questionType.questionType == "Sort">
	   			<#list questionPojo.answerList as answer>
	   				<table width="100%">
	   					<tr>
	   						<td colspan="2"><b>${questionLabelPojo.answerText} : </b>${answer.answerText}</td>
	   					</tr>
	   					<tr>
	   						<td width="50%"><b>${questionLabelPojo.validity} : </b>${answer.validity}</td>
	   						<td width="50%"><b>${questionLabelPojo.comment} : </b>${answer.comment}</td>
	   					</tr>
	   					<tr>
	   						<td width="50%"><b>${questionLabelPojo.author} : </b>${answer.autor.name} ${answer.autor.prename}</td>
	   						<td width="50%"><b>${questionLabelPojo.reviewer} : </b>${answer.rewiewer.name} ${answer.rewiewer.prename}</td>
	   					</tr>
	   				</table>
	   				<br />
	   				<br />	   				
	   			</#list>
	  		</#if>
	  		
	  		<#if questionPojo.question.questionType.questionType == "MCQ">
	  			<#list questionPojo.answerList as answer>
	   				<table width="100%">
	   					<tr>
	   						<td colspan="2">${answer.answerText}</td>
	   					</tr>
	   					<tr>
	   						<td width="50%"><b>${questionLabelPojo.validity} : </b>${answer.validity}</td>
	   						<td width="50%"><b>${questionLabelPojo.comment} : </b>${answer.comment}</td>
	   					</tr>
	   					<tr>
	   						<td width="50%"><b>${questionLabelPojo.author} : </b>${answer.autor.name} ${answer.autor.prename}</td>
	   						<td width="50%"><b>${questionLabelPojo.reviewer} : </b>${answer.rewiewer.name} ${answer.rewiewer.prename}</td>
	   					</tr>
	   				</table>
	   				<br />
	   				<br />	   				
	   			</#list>
	  		</#if>
	  		
	  		<#if questionPojo.question.questionType.questionType == "Imgkey">
	  			<#list questionPojo.answerList as answer>
	   				<table width="100%">
	   					<tr>
	   						<td colspan="2">${answer.answerText}</td>
	   					</tr>
	   					<tr>
	   						<td colspan="2"><b>${questionLabelPojo.additionalKeyword} : </b>${answer.additionalKeywords}</td>
	   					</tr>
	   					<tr>
	   						<td width="50%"><b>${questionLabelPojo.validity} : </b>${answer.validity}</td>
	   						<td width="50%"><b>${questionLabelPojo.comment} : </b>${answer.comment}</td>
	   					</tr>
	   					<tr>
	   						<td width="50%"><b>${questionLabelPojo.author} : </b>${answer.autor.name} ${answer.autor.prename}</td>
	   						<td width="50%"><b>${questionLabelPojo.reviewer} : </b>${answer.rewiewer.name} ${answer.rewiewer.prename}</td>
	   					</tr>
	   				</table>
	   				<br />
	   				<br />	   				
	   			</#list>
	  		</#if>
	  		
	  		<#if questionPojo.question.questionType.questionType == "ShowInImage">
	  			<#list questionPojo.answerList as answer>
	   				<table width="100%">
	   					<tr>
	   						<td colspan="2"><b>${questionLabelPojo.answerText} : </b>${answer.answerText}</td>
	   					</tr>
	   					<tr>
	   						<td colspan="2">${answer.mediaPath}</td>
	   					</tr>
	   					<tr>
	   						<td width="50%"><b>${questionLabelPojo.validity} : </b>${answer.validity}</td>
	   						<td width="50%"><b>${questionLabelPojo.comment} : </b>${answer.comment}</td>
	   					</tr>
	   					<tr>
	   						<td width="50%"><b>${questionLabelPojo.author} : </b>${answer.autor.name} ${answer.autor.prename}</td>
	   						<td width="50%"><b>${questionLabelPojo.reviewer} : </b>${answer.rewiewer.name} ${answer.rewiewer.prename}</td>
	   					</tr>
	   				</table>
	   				<br />
	   				<br />	   				
	   			</#list>
	  		</#if>
	  		
	  		<#if questionPojo.question.questionType.questionType == "Matrix">
	  			<table width="100%">
	  				<#list questionPojo.matrixAnswerList as matrixAnswer>
	  					<tr <#if matrixAnswer_index == 0>style="background-color:#CCCCCC"</#if>>
		  					<#list matrixAnswer as answer>
		  						<#if answer_index == 0 || matrixAnswer_index == 0>
		  							<th style="background-color:#CCCCCC">${answer}</th>
		  						<#else>
		  							<td style="background-color:#F5F5F5">${answer}</td>	
		  						</#if>	  						
		  					</#list>
	  					</tr>
	   				</#list>
	   			</table>
   				<table width="100%">
   					<tr>
	   					<td colspan="2"><b>${questionLabelPojo.comment} : </b>${questionPojo.question.answers[0].comment}</td>
	   				</tr>
	   				<tr>
	   					<td width="50%"><b>${questionLabelPojo.author} : </b>${questionPojo.question.answers[0].autor.name} ${questionPojo.question.answers[0].autor.prename}</td>
	   					<td width="50%"><b>${questionLabelPojo.reviewer} : </b>${questionPojo.question.answers[0].rewiewer.name} ${questionPojo.question.answers[0].rewiewer.prename}</td>
	   				</tr>
   				</table>
	  		</#if>
	   	</#if>	 
	  </#if>	 
	</div>
</#list>
