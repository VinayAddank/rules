package org.rta.rules.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rta.rules.model.DocTypesRuleModel;
import org.rta.rules.model.FeeRuleModel;
import org.rta.rules.model.TaxRuleModel;
import org.rta.rules.service.TaxFeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaxFeeController {

    private static final Log log = LogFactory.getLog(TaxFeeController.class);
   
    @Value("${rta.server.environment}")
	private String activeProfile;
    
    @Autowired
    public TaxFeeService taxFeeService;
    
    @RequestMapping(path = "/caltax", method = RequestMethod.POST,
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> calculateTax(HttpServletRequest request, @Valid @RequestBody TaxRuleModel taxRuleModel) {
        log.info(":calculateTax:Drools:start:" + taxRuleModel);
        try {
        	taxRuleModel = taxFeeService.getTaxDetails(taxRuleModel);
        	log.info(":calculateTax:::Drools:end:" + taxRuleModel);	
        } catch (Exception e) {
            log.error("Exception while saving calculateTax Drools: " + e.getMessage());
            
            e.printStackTrace();
            throw new IllegalArgumentException("Exception while saving calculateTax Drools:");
        }
        return ResponseEntity.status(HttpStatus.OK).body(taxRuleModel);
    }
    
    
    @RequestMapping(path = "/citizen/caltax", method = RequestMethod.POST,
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> calculateTaxForCitizen(HttpServletRequest request, @Valid @RequestBody TaxRuleModel taxRuleModel) {
        log.info(":calculateTaxForCitizen:Drools:start:" + taxRuleModel);
        try {
        	taxRuleModel = taxFeeService.getTaxDetailsForCitizen(taxRuleModel);
        	log.info(":calculateTaxForCitizen:::Drools:end:" + taxRuleModel);	
        } catch (Exception e) {
            log.error("Exception while saving calculateTaxForCitizen Drools: " + e.getMessage());
            
            e.printStackTrace();
            throw new IllegalArgumentException("Exception while saving calculateTaxForCitizen Drools:");
        }
        return ResponseEntity.status(HttpStatus.OK).body(taxRuleModel);
    }
    
    @RequestMapping(path = "/calfee", method = RequestMethod.POST,
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> calculateFee(HttpServletRequest request, @Valid @RequestBody FeeRuleModel feeRuleModel) {
        log.info("::::::calculateFee::Drools:::start:::::" + feeRuleModel);
        try {
        	feeRuleModel = taxFeeService.getFeeAmount(feeRuleModel);
        } catch (Exception e) {
            log.error("Exception while saving calculateFee Drools:: " + e.getMessage());
            e.printStackTrace();
            throw new IllegalArgumentException("Parameter(s) violating Some Data Integrity !");
        }
        log.info("::::::::calculateFee::Drools:::end::::: " + feeRuleModel);
        return ResponseEntity.status(HttpStatus.OK).body(feeRuleModel);
    }
    
    @RequestMapping(path = "/citizen/calfee", method = RequestMethod.POST,
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> calculateFeeForCitizen(HttpServletRequest request, @Valid @RequestBody FeeRuleModel feeRuleModel) {
        log.info("::::::calculateFeeForCitizen::Drools:::start:::::" + feeRuleModel);
        try {
        	feeRuleModel = taxFeeService.getFeeAmountForCitizen(feeRuleModel);
        } catch (Exception e) {
            log.error("Exception while saving calculateFeeForCitizen Drools:: " + e.getMessage());
            e.printStackTrace();
            throw new IllegalArgumentException("Parameter(s) violating Some Data Integrity !");
        }
        log.info("::::::::calculateFeeForCitizen::Drools:::end::::: " + feeRuleModel);
        return ResponseEntity.status(HttpStatus.OK).body(feeRuleModel);
    }
    
    @RequestMapping(path = "/license/calfee", method = RequestMethod.POST,
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> calculateLicenseFee(HttpServletRequest request, @Valid @RequestBody FeeRuleModel feeRuleModel) {
        log.info("::::::::calculateLicenseFee::Drools:::start:::::" + feeRuleModel);
        try {
        	feeRuleModel = taxFeeService.getLicenseFeeAmount(feeRuleModel);
        } catch (Exception e) {
            log.error("Exception while saving calculateLicenseFee Drools:: " + e.getMessage());
            e.printStackTrace();
            throw new IllegalArgumentException("Parameter(s) violating Some Data Integrity !");
        }
        log.info("::::::::calculateTax::Drools:::end:::::");
        return ResponseEntity.status(HttpStatus.OK).body(feeRuleModel);
    }
    
    @RequestMapping(path = "/test/api", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})    			 
    		   public ResponseEntity<?> testAPI(HttpServletRequest request) {    		 
    		      log.info("::::::::testAPI::Drools:::::");    
    		      DocTypesRuleModel docTypesRuleModel = new DocTypesRuleModel();
    		      docTypesRuleModel.setName(activeProfile);
    		     return ResponseEntity.status(HttpStatus.OK).body(docTypesRuleModel);
    		 }
    
    
    
 }
