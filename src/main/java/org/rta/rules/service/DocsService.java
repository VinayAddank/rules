package org.rta.rules.service;

import java.util.List;

import org.rta.rules.model.CitizenRequest;
import org.rta.rules.model.DocPermissionModel;
import org.rta.rules.model.DocTypesRuleModel;

/**
 *	@Author sohan.maurya created on Nov 8, 2016.
 */
public interface DocsService {

    public List<DocTypesRuleModel> getRegistrationAttachmentDocs(DocPermissionModel model);

    public List<DocTypesRuleModel> getCitizenAttachmentDocs(CitizenRequest citizenRequest);
}
