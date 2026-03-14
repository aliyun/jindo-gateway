package com.aliyun.jindodata.gateway.call;

import com.aliyun.jindodata.gateway.common.JfsRequestOptions;
import com.aliyun.jindodata.gateway.common.JfsStatus;
import com.aliyun.jindodata.gateway.http.reponse.JfsCheckAccessResponse;
import com.aliyun.jindodata.gateway.http.request.JfsCheckAccessRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JfsCheckAccessCall extends JfsBaseCall {
    private static final Logger LOG = LoggerFactory.getLogger(JfsCheckAccessCall.class);

    public JfsCheckAccessCall() {
        request = new JfsCheckAccessRequest();
        response = new JfsCheckAccessResponse();
    }

    @Override
    public JfsStatus execute(JfsRequestOptions requestOptions) {
        long startTime = System.currentTimeMillis();
        JfsCheckAccessRequest req = (JfsCheckAccessRequest) request;

        String path = req.getPath();
        int fsAction = req.getFsAction();
        String bucket = requestOptions.getBucket();

        LOG.info("CheckAccess path {} fsAction {} from bucket {}", path, fsAction, bucket);

        JfsStatus status = super.execute(requestOptions);

        if (status.isOk()) {
            long elapsed = System.currentTimeMillis() - startTime;
            String requestId = response.getRequestId();
            String serverTime = response.getServerTime();

            LOG.info("[RequestId: {}] Successfully CheckAccess path {} fsAction {} from bucket {} dur {}ms ossServerElapsed {}",
                    requestId, path, fsAction, bucket, elapsed, serverTime);
        } else {
            String requestId = response.getRequestId();
            LOG.warn("[RequestId: {}] Failed to CheckAccess path {} fsAction {} from bucket {}, errorCode: {}, errorMessage: {}",
                    requestId, path, fsAction, bucket, status.getCode(), status.getMessage());
        }

        return status;
    }

    public void setPath(String path) {
        request.setPath(path);
    }

    public void setFsAction(int fsAction) {
        ((JfsCheckAccessRequest) request).setFsAction(fsAction);
    }
}
