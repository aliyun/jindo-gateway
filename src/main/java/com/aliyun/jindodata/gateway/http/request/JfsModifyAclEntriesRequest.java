package com.aliyun.jindodata.gateway.http.request;

import com.aliyun.jindodata.gateway.common.JfsRequestOptions;
import org.apache.hadoop.fs.permission.AclEntry;

import java.util.List;

public class JfsModifyAclEntriesRequest extends JfsAbstractHttpRequest {
    private static final String REQUEST_TYPE = "modifyAclEntries";
    private static final String SRC_KEY = "src";
    private static final String ACL_SPEC_KEY = "aclSpec";

    private List<AclEntry> aclSpec;

    public JfsModifyAclEntriesRequest() {
        super();
        setQueryParam(NS_DFS, "");
    }

    @Override
    public void prepareRequest(JfsRequestOptions options) {
        initRequestWithOptions(options);
        initRequestXml(REQUEST_TYPE);

        requestXml.addRequestParameter(SRC_KEY, encodePath(path));
        requestXml.addRequestParameter(ACL_SPEC_KEY, aclSpec);

        setBody(requestXml.getXmlString());
    }

    public String getSrc() {
        return path;
    }

    public void setSrc(String src) {
        this.path = src;
    }

    public List<AclEntry> getAclSpec() {
        return aclSpec;
    }

    public void setAclSpec(List<AclEntry> aclSpec) {
        this.aclSpec = aclSpec;
    }
}
