package org.rta.rules;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.rta.rules.enums.ServiceType;
import org.rta.rules.model.FeeRuleModel;
import org.rta.rules.model.TaxRuleModel;

public class KieSessions {
    private static KieServices ks = KieServices.Factory.get();
    private static KieContainer kContainer = ks.getKieClasspathContainer();

    public static KieSession getKieSessionForRegistrationAttachments() {
        KieSession kSession = null;
        try {
            kSession = kContainer.newKieSession("registration-attachment-rules");
        } catch (Exception t) {
            t.printStackTrace();
        }
        return kSession;
    }

    public static KieSession getKieSessionForCitizenAttachments() {
        KieSession kSession = null;
        try {
            kSession = kContainer.newKieSession("citizen-attachment-rules");
        } catch (Exception t) {
            t.printStackTrace();
        }
        return kSession;
    }

    
    public static KieSession getKieSessionForTax(TaxRuleModel taxRuleModel) {
        KieSession kSession = null;
        try {
           kSession = kContainer.newKieSession("vehicle-tax-rules");
        } catch (Exception t) {
            t.printStackTrace();
        }
        return kSession;
    }
    
    public static KieSession getKieSessionForTaxExempted(TaxRuleModel taxRuleModel) {
        KieSession kSession = null;
        try {
           kSession = kContainer.newKieSession("exempted-tax-rules");
        } catch (Exception t) {
            t.printStackTrace();
        }
        return kSession;
    }
    
    
    public static KieSession getKieSessionForNTTax(TaxRuleModel taxRuleModel) {
        KieSession kSession = null;
        try {
           // if (taxRuleModel.getGreenTax())
             //   kSession = kContainer.newKieSession("green-tax-rule");
            //else
                kSession = kContainer.newKieSession("nontransport-tax-rules");
        } catch (Exception t) {
            t.printStackTrace();
        }
        return kSession;
    }

    public static KieSession getKieSessionForTTax(TaxRuleModel taxRuleModel) {
        KieSession kSession = null;
        kSession = kContainer.newKieSession("transport-tax-rules");
        return kSession;
    }

    public static KieSession getKieSessionForNTFee(FeeRuleModel feeRuleModel) {
        KieSession kSession = null;
        switch (ServiceType.getServiceType(feeRuleModel.getServiceCode())) {
        case FRESH_RC_FINANCIER:
            kSession = kContainer.newKieSession("nontransport-refresh-rc-fee-rules");
            break;
        case HPA:
            kSession = kContainer.newKieSession("nontransport-hpa-fee-rules");
            break;
        case HPT:
            kSession = kContainer.newKieSession("nontransport-hpt-fee-rules");
            break;
        case NOC_CANCELLATION:
            kSession = kContainer.newKieSession("nontransport-cancelnoc-fee-rules");
            break;
        case NOC_ISSUE:
            kSession = kContainer.newKieSession("nontransport-issuenoc-fee-rules");
            break;
        case FRESH_REGISTRATION:
        case PAY_TAX:
            kSession = kContainer.newKieSession("nontransport-fresh-reg-fee-rules");
            break;
        case ADDRESS_CHANGE:
            kSession = kContainer.newKieSession("nontransport-changeaddress-fee-rules");
            break;
        case DUPLICATE_REGISTRATION:
            kSession = kContainer.newKieSession("nontransport-duplicateregist-fee-rules");
            break;
        case VEHICLE_REASSIGNMENT:
            kSession = kContainer.newKieSession("nontransport-vehiclreassign-fee-rules");
            break;
        case SUSPENSION_REVOCATION:
            kSession = kContainer.newKieSession("nontransport-vehiclreassign-fee-rules");
            break;
        case THEFT_INTIMATION:
            kSession = kContainer.newKieSession("nontransport-vehiclreassign-fee-rules");
            break;
        case VEHICLE_ATLERATION:
            kSession = kContainer.newKieSession("nontransport-vehiclalalteration-fee-rules");
            break;
        case REGISTRATION_RENEWAL:
            kSession = kContainer.newKieSession("nontransport-renewal-fee-rules");
            break;
        case DIFFERENTIAL_TAX:
            kSession = kContainer.newKieSession("nontransport-fresh-reg-fee-rules");
            break;
        case OWNERSHIP_TRANSFER_DEATH:
            kSession = kContainer.newKieSession("nontransport-tow-fee-rules");
            break;
        case OWNERSHIP_TRANSFER_AUCTION:
            kSession = kContainer.newKieSession("nontransport-tow-fee-rules");
            break;
        case OWNERSHIP_TRANSFER_SALE:
            kSession = kContainer.newKieSession("nontransport-tow-fee-rules");
            break;
        case FC_FRESH:
        case FC_OTHER_STATION:
        case FC_RENEWAL:
        case FC_REVOCATION_CFX:
        case FC_RE_INSPECTION_SB:
            kSession = kContainer.newKieSession("fitness-otherstation-fee-rules");
            break;
        case PERMIT_FRESH:
        case PERMIT_RENEWAL:
        case PERMIT_RENEWAL_AUTH_CARD:
        case PERMIT_SURRENDER:
            kSession = kContainer.newKieSession("permit-fee-rules");
            break;
        case STOPPAGE_TAX:
        case STOPPAGE_TAX_REVOCATION:
            kSession = kContainer.newKieSession("stoppage-tax-fee-rules");
            break;
        }
        return kSession;
    }

    public static KieSession getKieSessionForTFee(FeeRuleModel feeRuleModel) {
        KieSession kSession = null;
        switch (ServiceType.getServiceType(feeRuleModel.getServiceCode())) {
        case FRESH_RC_FINANCIER:
            kSession = kContainer.newKieSession("transport-refresh-rc-fee-rules");
            break;
        case HPA:
            kSession = kContainer.newKieSession("transport-hpa-fee-rules");
            break;
        case HPT:
            kSession = kContainer.newKieSession("transport-hpt-fee-rules");
            break;
        case NOC_CANCELLATION:
            kSession = kContainer.newKieSession("transport-cancelnoc-fee-rules");
            break;
        case NOC_ISSUE:
            kSession = kContainer.newKieSession("transport-issuenoc-fee-rules");
            break;
        case FRESH_REGISTRATION:
        case PAY_TAX:
            kSession = kContainer.newKieSession("transport-fresh-reg-fee-rules");
            break;
        case ADDRESS_CHANGE:
            kSession = kContainer.newKieSession("transport-changeaddress-fee-rules");
            break;
        case DUPLICATE_REGISTRATION:
            kSession = kContainer.newKieSession("transport-duplicateregist-fee-rules");
            break;
        case VEHICLE_REASSIGNMENT:
            kSession = kContainer.newKieSession("transport-vehiclreassign-fee-rules");
            break;
        case SUSPENSION_REVOCATION:
            kSession = kContainer.newKieSession("transport-vehiclreassign-fee-rules");
            break;
        case THEFT_INTIMATION:
            kSession = kContainer.newKieSession("transport-vehiclreassign-fee-rules");
            break;
        case VEHICLE_ATLERATION:
            kSession = kContainer.newKieSession("transport-vehiclalalteration-fee-rules");
            break;
        case REGISTRATION_RENEWAL:
            kSession = kContainer.newKieSession("transport-renewal-fee-rules");
            break;
        case DIFFERENTIAL_TAX:
            kSession = kContainer.newKieSession("transport-fresh-reg-fee-rules");
            break;
        case OWNERSHIP_TRANSFER_DEATH:
            kSession = kContainer.newKieSession("transport-tow-fee-rules");
            break;
        case OWNERSHIP_TRANSFER_AUCTION:
            kSession = kContainer.newKieSession("transport-tow-fee-rules");
            break;
        case OWNERSHIP_TRANSFER_SALE:
            kSession = kContainer.newKieSession("transport-tow-fee-rules");
            break;
        case FC_FRESH:
            kSession = kContainer.newKieSession("fitness-fresh-fee-rules");
            break;
        case FC_OTHER_STATION:
            kSession = kContainer.newKieSession("fitness-otherstation-fee-rules");
            break;
        case FC_RENEWAL:
            kSession = kContainer.newKieSession("fitness-renewal-fee-rules");
            break;
        case FC_REVOCATION_CFX:
            kSession = kContainer.newKieSession("fitness-revocation-fee-rules");
            break;
        case FC_RE_INSPECTION_SB:
            kSession = kContainer.newKieSession("fitness-reinspection-fee-rules");
            break;
        case PERMIT_FRESH:
            kSession = kContainer.newKieSession("permit-fresh-fee-rules");
            break;
        case PERMIT_RENEWAL:
            kSession = kContainer.newKieSession("permit-renewal-fee-rules");
            break;
        case PERMIT_RENEWAL_AUTH_CARD:
            kSession = kContainer.newKieSession("permit-renewalauth-fee-rules");
            break;
        case PERMIT_SURRENDER:
            kSession = kContainer.newKieSession("permit-surrender-fee-rules");
            break;
        case PERMIT_TRANSFER:
            kSession = kContainer.newKieSession("permit-transfer-fee-rules");
            break;
        case PERMIT_REPLACEMENT_VEHICLE:
            kSession = kContainer.newKieSession("permit-replacevehicle-fee-rules");
            break;
        case PERMIT_VARIATIONS:
            kSession = kContainer.newKieSession("permit-variations-fee-rules");
            break;
        case STOPPAGE_TAX:
        case STOPPAGE_TAX_REVOCATION:
            kSession = kContainer.newKieSession("stoppage-tax-fee-rules");
            break;
        }
        return kSession;
    }

    public static KieSession getKieSessionForLicenseFee(FeeRuleModel feeRuleModel) {
        KieSession kSession = null;
        kSession = kContainer.newKieSession("license-fee-rules");
        return kSession;
    }
    
    public static KieSession getKieSessionForHSRP(FeeRuleModel feeRuleModel) {
        KieSession kSession = null;
        kSession = kContainer.newKieSession("hsrp-fee-rules");
        return kSession;
    }
    
    public static KieSession getKieSessionForGreenTax(TaxRuleModel taxRuleModel) {
        KieSession kSession = null;
        try {
            kSession = kContainer.newKieSession("green-tax-rule");
        } catch (Exception t) {
            t.printStackTrace();
        }
        return kSession;
    }
}
