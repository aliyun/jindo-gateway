package com.aliyun.jindodata.gateway.http.request;

import com.aliyun.jindodata.gateway.common.JfsRequestOptions;
import org.apache.hadoop.fs.XAttr;

import java.util.List;

public class JfsGetXAttrsRequest extends JfsAbstractHttpRequest {
    private static final String REQUEST_TYPE = "getXAttrs";
    private static final String SRC_KEY = "src";
    private static final String XATTRS_KEY = "xAttrs";

    private List<XAttr> xAttrs;

    public JfsGetXAttrsRequest() {
        super();
        setQueryParam(NS_DFS, "");
    }

    @Override
    public void prepareRequest(JfsRequestOptions options) {
        initRequestWithOptions(options);
        initRequestXml(REQUEST_TYPE);

        requestXml.addRequestParameter(SRC_KEY, encodePath(path));
        requestXml.addRequestParameterXAttrs(XATTRS_KEY, xAttrs);

        setBody(requestXml.getXmlString());
    }

    public String getSrc() {
        return path;
    }

    public void setSrc(String src) {
        this.path = src;
    }

    public List<XAttr> getXAttrs() {
        return xAttrs;
    }

    public void setXAttrs(List<XAttr> xAttrs) {
        this.xAttrs = xAttrs;
    }
}
