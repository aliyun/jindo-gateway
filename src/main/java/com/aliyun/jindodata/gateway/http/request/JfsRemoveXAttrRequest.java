package com.aliyun.jindodata.gateway.http.request;

import com.aliyun.jindodata.gateway.common.JfsRequestOptions;
import org.apache.hadoop.fs.XAttr;

public class JfsRemoveXAttrRequest extends JfsAbstractHttpRequest {
    private static final String REQUEST_TYPE = "removeXAttr";
    private static final String SRC_KEY = "src";
    private static final String XATTR_KEY = "xAttr";

    private XAttr xAttr;

    public JfsRemoveXAttrRequest() {
        super();
        setQueryParam(NS_DFS, "");
    }

    @Override
    public void prepareRequest(JfsRequestOptions options) {
        initRequestWithOptions(options);
        initRequestXml(REQUEST_TYPE);

        requestXml.addRequestParameter(SRC_KEY, encodePath(path));
        requestXml.addRequestParameter(XATTR_KEY, xAttr);

        setBody(requestXml.getXmlString());
    }

    public String getSrc() {
        return path;
    }

    public void setSrc(String src) {
        this.path = src;
    }

    public XAttr getXAttr() {
        return xAttr;
    }

    public void setXAttr(XAttr xAttr) {
        this.xAttr = xAttr;
    }
}
