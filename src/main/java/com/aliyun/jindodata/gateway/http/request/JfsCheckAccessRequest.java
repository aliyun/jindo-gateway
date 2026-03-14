package com.aliyun.jindodata.gateway.http.request;

import com.aliyun.jindodata.gateway.common.JfsRequestOptions;

public class JfsCheckAccessRequest extends JfsAbstractHttpRequest {
    private static final String REQUEST_TYPE = "checkAccess";
    private static final String PATH_KEY = "path";
    private static final String FS_ACTION_KEY = "fsAction";

    private int fsAction;

    public JfsCheckAccessRequest() {
        super();
        setQueryParam(NS_DFS, "");
    }

    @Override
    public void prepareRequest(JfsRequestOptions options) {
        initRequestWithOptions(options);
        initRequestXml(REQUEST_TYPE);

        requestXml.addRequestParameter(PATH_KEY, encodePath(path));
        requestXml.addRequestParameter(FS_ACTION_KEY, fsAction);

        setBody(requestXml.getXmlString());
    }

    public int getFsAction() {
        return fsAction;
    }

    public void setFsAction(int fsAction) {
        this.fsAction = fsAction;
    }
}
