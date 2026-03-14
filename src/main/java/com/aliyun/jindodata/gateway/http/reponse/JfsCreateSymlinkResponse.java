package com.aliyun.jindodata.gateway.http.reponse;

import com.aliyun.jindodata.gateway.common.JfsStatus;

public class JfsCreateSymlinkResponse extends JfsAbstractHttpResponse {

    public JfsCreateSymlinkResponse() {
        super();
    }

    @Override
    public JfsStatus parseXml(String xml) {
        return JfsStatus.OK();
    }
}
