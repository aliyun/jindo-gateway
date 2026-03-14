package com.aliyun.jindodata.gateway.http.reponse;

import com.aliyun.jindodata.gateway.common.JfsStatus;

public class JfsCheckAccessResponse extends JfsAbstractHttpResponse {

    public JfsCheckAccessResponse() {
        super();
    }

    @Override
    public JfsStatus parseXml(String xml) {
        return JfsStatus.OK();
    }
}
