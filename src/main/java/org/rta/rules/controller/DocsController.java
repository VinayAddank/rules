package org.rta.rules.controller;



import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.rta.rules.enums.AlterationCategory;
import org.rta.rules.enums.ServiceType;
import org.rta.rules.enums.UserType;
import org.rta.rules.model.CitizenRequest;
import org.rta.rules.model.DocPermissionModel;
import org.rta.rules.model.DocTypesRuleModel;
import org.rta.rules.service.DocsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class DocsController {
	
    @Autowired
    private DocsService docsService;
    private static final Logger log = Logger.getLogger(DocsController.class);

    @RequestMapping(value = {"/regdocuments"}, method = RequestMethod.POST)
    public ResponseEntity<?> getRegistrationDocuments(@Valid @RequestBody DocPermissionModel model) {
    	List<DocTypesRuleModel> models =null;
    	model = getDocPermissionModel(model);
    	models = docsService.getRegistrationAttachmentDocs(model);
    	return ResponseEntity.ok(models);
	}

    @RequestMapping(value = {"/citdocuments"}, method = RequestMethod.PUT)
    public ResponseEntity<?> getCitizenAttachments(@RequestBody DocPermissionModel model) {
    	log.info("DocsController -- "+ "model- " + model);
    	log.info("DocsController -- "+ "getAlterationCategory- " + model.getAlterationCategory());
        CitizenRequest citizenRequest = new CitizenRequest();
        citizenRequest.setServicesType(ServiceType.getServiceType(model.getServiceTypeCode()));
        citizenRequest.setAge(model.getCustomerAge());
        if(model.getAlterationCategory()!=null){
        citizenRequest.setAlterationCategory(model.getAlterationCategory());
        }else{
        	List<AlterationCategory> alterationCategories=new ArrayList<>();
        	citizenRequest.setAlterationCategory(alterationCategories);
        }
        citizenRequest.setIsAadhar(true);
        citizenRequest.setRegistrationCategoryType(model.getRegistrationCategoryType());
        citizenRequest.setIsBadge(model.getIsBadge());
        citizenRequest.setIsInsuraceDocs(model.getIsInsuraceDocs());
        citizenRequest.setStatus(model.getStatus());
        //citizenRequest.setUserType(UserType.getUserType(model.getUserType().getLabel()));
        citizenRequest.setUserType(model.getUserType());
        System.out.println("test ------------ " + citizenRequest.getServicesType());
        return ResponseEntity.ok(docsService.getCitizenAttachmentDocs(citizenRequest));
    }

    private DocPermissionModel getDocPermissionModel( DocPermissionModel model){
    	
   	 	if(model.getUserType() != UserType.ROLE_DEALER){
	   	 	model.setOwnershipType(null);
		    model.setVehicleClass("KKKKKKKKKKK");
		    model.setInvoiceValue(0d);
		    model.setIsFinance(false);
           	model.setCustomerAge(100);
            model.setIsCustomerDisabled(false);
   	 	}
        return model;
    }
}