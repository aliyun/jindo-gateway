package com.aliyun.jindodata.gateway.http.request;

import com.aliyun.jindodata.gateway.common.JfsRequestOptions;

public class JfsSetTimesRequest extends JfsAbstractHttpRequest {
    private static final String REQUEST_TYPE = "setTimes";
    private static final String PATH_KEY = "path";
    private static final String ATIME_KEY = "atime";
    private static final String MTIME_KEY = "mtime";

    private long atime;
    private long mtime;

    public JfsSetTimesRequest() {
        super();
        setQueryParam(NS_DFS, "");
    }

    @Override
    public void prepareRequest(JfsRequestOptions options) {
        initRequestWithOptions(options);
        initRequestXml(REQUEST_TYPE);

        requestXml.addRequestParameter(PATH_KEY, encodePath(path));
        requestXml.addRequestParameter(ATIME_KEY, atime);
        requestXml.addRequestParameter(MTIME_KEY, mtime);

        setBody(requestXml.getXmlString());
    }

    public long getAtime() {
        return atime;
    }

    public void setAtime(long atime) {
        this.atime = atime;
    }

    public long getMtime() {
        return mtime;
    }

    public void setMtime(long mtime) {
        this.mtime = mtime;
    }
}
