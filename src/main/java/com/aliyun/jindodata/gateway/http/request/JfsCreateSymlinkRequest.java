package com.aliyun.jindodata.gateway.http.request;

import com.aliyun.jindodata.gateway.common.JfsRequestOptions;

public class JfsCreateSymlinkRequest extends JfsAbstractHttpRequest {
    private static final String REQUEST_TYPE = "createSymlink";
    private static final String TARGET_KEY = "target";
    private static final String LINK_KEY = "link";
    private static final String PERMISSION_KEY = "permission";
    private static final String CREATE_PARENT_KEY = "createParent";

    private String target;
    private String link;
    private short permission;
    private boolean createParent;

    public JfsCreateSymlinkRequest() {
        super();
        setQueryParam(NS_DFS, "");
    }

    @Override
    public void prepareRequest(JfsRequestOptions options) {
        initRequestWithOptions(options);
        initRequestXml(REQUEST_TYPE);

        requestXml.addRequestParameter(TARGET_KEY, encodePath(target));
        requestXml.addRequestParameter(LINK_KEY, encodePath(link));
        requestXml.addRequestParameter(PERMISSION_KEY, permission);
        requestXml.addRequestParameter(CREATE_PARENT_KEY, createParent);

        setBody(requestXml.getXmlString());
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public short getPermission() {
        return permission;
    }

    public void setPermission(short permission) {
        this.permission = permission;
    }

    public boolean isCreateParent() {
        return createParent;
    }

    public void setCreateParent(boolean createParent) {
        this.createParent = createParent;
    }
}
