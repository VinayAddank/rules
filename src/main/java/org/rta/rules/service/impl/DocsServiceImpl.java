package org.rta.rules.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.kie.api.runtime.KieSession;
import org.rta.rules.KieSessions;
import org.rta.rules.model.CitizenRequest;
import org.rta.rules.model.DocPermissionModel;
import org.rta.rules.model.DocTypesRuleModel;
import org.rta.rules.service.DocsService;
import org.springframework.stereotype.Service;

/**
 *	@Author sohan.maurya created on Nov 8, 2016.
 */
@Service("docsService")
public class DocsServiceImpl implements DocsService {


    @SuppressWarnings("unchecked")
    @Override
    public List<DocTypesRuleModel> getRegistrationAttachmentDocs(DocPermissionModel model) {
        KieSession kSession = KieSessions.getKieSessionForRegistrationAttachments();
        kSession.setGlobal("regDocTypeList", new ArrayList<DocTypesRuleModel>());

        kSession.insert(model);
        int rules = kSession.fireAllRules();
        System.out.println(rules);
        List<DocTypesRuleModel> testList = (List<DocTypesRuleModel>) kSession.getGlobal("regDocTypeList");
        System.out.println("regDocTypeList" + testList.size());
        return (List<DocTypesRuleModel>) kSession.getGlobal("regDocTypeList");
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<DocTypesRuleModel> getCitizenAttachmentDocs(CitizenRequest citizenRequest) {

        KieSession kSession = KieSessions.getKieSessionForCitizenAttachments();
        kSession.setGlobal("citDocTypeList", new ArrayList<DocTypesRuleModel>());
        kSession.insert(citizenRequest);
        int rules = kSession.fireAllRules();
        return (List<DocTypesRuleModel>) kSession.getGlobal("citDocTypeList");
    }


}
