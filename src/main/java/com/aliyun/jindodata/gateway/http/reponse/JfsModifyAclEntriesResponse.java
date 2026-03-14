package com.aliyun.jindodata.gateway.http.reponse;

import com.aliyun.jindodata.gateway.common.JfsStatus;

public class JfsModifyAclEntriesResponse extends JfsAbstractHttpResponse {

    public JfsModifyAclEntriesResponse() {
        super();
    }

    @Override
    public JfsStatus parseXml(String xml) {
        return JfsStatus.OK();
    }
}
