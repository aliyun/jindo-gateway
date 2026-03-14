package com.aliyun.jindodata.gateway.http.reponse;

import com.aliyun.jindodata.gateway.common.JfsStatus;

public class JfsRemoveAclEntriesResponse extends JfsAbstractHttpResponse {

    public JfsRemoveAclEntriesResponse() {
        super();
    }

    @Override
    public JfsStatus parseXml(String xml) {
        return JfsStatus.OK();
    }
}
