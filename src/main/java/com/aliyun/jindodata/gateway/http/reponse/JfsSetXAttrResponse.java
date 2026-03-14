package com.aliyun.jindodata.gateway.http.reponse;

import com.aliyun.jindodata.gateway.common.JfsStatus;

public class JfsSetXAttrResponse extends JfsAbstractHttpResponse {

    @Override
    public JfsStatus parseXml(String xml) {
        return JfsStatus.OK();
    }
}
