package com.aliyun.jindodata.gateway.call;

import com.aliyun.jindodata.gateway.common.JfsFileStatus;
import com.aliyun.jindodata.gateway.common.JfsRequestOptions;
import com.aliyun.jindodata.gateway.common.JfsStatus;
import com.aliyun.jindodata.gateway.common.JfsUtil;
import com.aliyun.jindodata.gateway.http.reponse.JfsGetFileLinkInfoResponse;
import com.aliyun.jindodata.gateway.http.request.JfsGetFileLinkInfoRequest;
import org.apache.hadoop.hdfs.protocol.HdfsFileStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JfsGetFileLinkInfoCall extends JfsBaseCall {
    private static final Logger LOG = LoggerFactory.getLogger(JfsGetFileLinkInfoCall.class);

    private HdfsFileStatus fileStatus;

    public JfsGetFileLinkInfoCall() {
        request = new JfsGetFileLinkInfoRequest();
        response = new JfsGetFileLinkInfoResponse();
    }

    @Override
    public JfsStatus execute(JfsRequestOptions requestOptions) {
        long startTime = System.currentTimeMillis();
        JfsGetFileLinkInfoRequest req = (JfsGetFileLinkInfoRequest) request;

        String path = req.getPath();
        String bucket = requestOptions.getBucket();

        LOG.info("GetFileLinkInfo path {} from bucket {}", path, bucket);

        JfsStatus status = super.execute(requestOptions);

        if (status.isOk()) {
            long elapsed = System.currentTimeMillis() - startTime;
            String requestId = response.getRequestId();
            String serverTime = response.getServerTime();

            LOG.info("[RequestId: {}] Successfully GetFileLinkInfo path {} from bucket {} dur {}ms ossServerElapsed {}",
                    requestId, path, bucket, elapsed, serverTime);
        } else {
            String requestId = response.getRequestId();
            LOG.warn("[RequestId: {}] Failed to GetFileLinkInfo path {} from bucket {}, errorCode: {}, errorMessage: {}",
                    requestId, path, bucket, status.getCode(), status.getMessage());
        }

        return status;
    }

    @Override
    protected void processResponse() {
        fileStatus = JfsUtil.convert2HdfsFileStatus(((JfsGetFileLinkInfoResponse) response).getFileStatus());
    }

    public void setPath(String path) {
        ((JfsGetFileLinkInfoRequest) request).setPath(path);
    }

    public HdfsFileStatus getFileStatus() {
        return fileStatus;
    }
}
