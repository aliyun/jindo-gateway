package com.aliyun.jindodata.gateway.http.request;

import com.aliyun.jindodata.gateway.common.JfsRequestOptions;

public class JfsRemoveAclRequest extends JfsAbstractHttpRequest {
    private static final String REQUEST_TYPE = "removeAcl";
    private static final String SRC_KEY = "src";

    public JfsRemoveAclRequest() {
        super();
        setQueryParam(NS_DFS, "");
    }

    @Override
    public void prepareRequest(JfsRequestOptions options) {
        initRequestWithOptions(options);
        initRequestXml(REQUEST_TYPE);

        requestXml.addRequestParameter(SRC_KEY, encodePath(path));

        setBody(requestXml.getXmlString());
    }

    public String getSrc() {
        return path;
    }

    public void setSrc(String src) {
        this.path = src;
    }
}
