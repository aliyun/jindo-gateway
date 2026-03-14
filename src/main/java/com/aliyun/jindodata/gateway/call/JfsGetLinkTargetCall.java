package com.aliyun.jindodata.gateway.call;

import com.aliyun.jindodata.gateway.common.JfsRequestOptions;
import com.aliyun.jindodata.gateway.common.JfsStatus;
import com.aliyun.jindodata.gateway.http.reponse.JfsGetLinkTargetResponse;
import com.aliyun.jindodata.gateway.http.request.JfsGetLinkTargetRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JfsGetLinkTargetCall extends JfsBaseCall {
    private static final Logger LOG = LoggerFactory.getLogger(JfsGetLinkTargetCall.class);

    public JfsGetLinkTargetCall() {
        request = new JfsGetLinkTargetRequest();
        response = new JfsGetLinkTargetResponse();
    }

    @Override
    public JfsStatus execute(JfsRequestOptions requestOptions) {
        long startTime = System.currentTimeMillis();
        JfsGetLinkTargetRequest req = (JfsGetLinkTargetRequest) request;

        String path = req.getPath();
        String bucket = requestOptions.getBucket();

        LOG.info("GetLinkTarget path {} from bucket {}", path, bucket);

        JfsStatus status = super.execute(requestOptions);

        if (status.isOk()) {
            long elapsed = System.currentTimeMillis() - startTime;
            String requestId = response.getRequestId();
            String serverTime = response.getServerTime();

            LOG.info("[RequestId: {}] Successfully GetLinkTarget path {} from bucket {} dur {}ms ossServerElapsed {}",
                    requestId, path, bucket, elapsed, serverTime);
        } else {
            String requestId = response.getRequestId();
            LOG.warn("[RequestId: {}] Failed to GetLinkTarget path {} from bucket {}, errorCode: {}, errorMessage: {}",
                    requestId, path, bucket, status.getCode(), status.getMessage());
        }

        return status;
    }

    public void setPath(String path) {
        request.setPath(path);
    }

    public String getTargetPath() {
        return ((JfsGetLinkTargetResponse) response).getTargetPath();
    }
}
