package com.aliyun.jindodata.gateway.http.reponse;

import com.aliyun.jindodata.gateway.common.JfsStatus;

public class JfsRemoveAclResponse extends JfsAbstractHttpResponse {

    public JfsRemoveAclResponse() {
        super();
    }

    @Override
    public JfsStatus parseXml(String xml) {
        // removeAcl 响应只需要检查状态，无额外数据需要解析
        return JfsStatus.OK();
    }
}
