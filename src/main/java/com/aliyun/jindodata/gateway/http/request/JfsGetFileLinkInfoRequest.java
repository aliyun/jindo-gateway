package com.aliyun.jindodata.gateway.http.request;

import com.aliyun.jindodata.gateway.common.JfsRequestOptions;

public class JfsGetFileLinkInfoRequest extends JfsAbstractHttpRequest {
    private static final String REQUEST_TYPE = "getFileLinkInfo";
    private static final String PATH_KEY = "path";

    public JfsGetFileLinkInfoRequest() {
        super();
        setQueryParam(NS_DFS, "");
    }

    @Override
    public void prepareRequest(JfsRequestOptions options) {
        initRequestWithOptions(options);
        initRequestXml(REQUEST_TYPE);

        requestXml.addRequestParameter(PATH_KEY, encodePath(path));

        setBody(requestXml.getXmlString());
    }
}
