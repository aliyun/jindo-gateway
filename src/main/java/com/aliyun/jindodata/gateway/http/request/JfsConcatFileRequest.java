package com.aliyun.jindodata.gateway.http.request;

import com.aliyun.jindodata.gateway.common.JfsRequestOptions;

public class JfsConcatFileRequest extends JfsAbstractHttpRequest {
    private static final String REQUEST_TYPE = "concatFile";
    private static final String PATH_KEY = "path";
    private static final String SOURCES_KEY = "sources";

    private String[] sources;

    public JfsConcatFileRequest() {
        super();
        setQueryParam(NS_DFS, "");
    }

    @Override
    public void prepareRequest(JfsRequestOptions options) {
        initRequestWithOptions(options);
        initRequestXml(REQUEST_TYPE);

        requestXml.addRequestParameter(PATH_KEY, encodePath(path));
        requestXml.addRequestParameterSources(SOURCES_KEY, sources);

        setBody(requestXml.getXmlString());
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String[] getSources() {
        return sources;
    }

    public void setSources(String[] sources) {
        this.sources = sources;
    }
}
