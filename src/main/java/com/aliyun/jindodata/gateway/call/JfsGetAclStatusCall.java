package com.aliyun.jindodata.gateway.call;

import com.aliyun.jindodata.gateway.common.JfsRequestOptions;
import com.aliyun.jindodata.gateway.common.JfsStatus;
import com.aliyun.jindodata.gateway.http.reponse.JfsGetAclStatusResponse;
import com.aliyun.jindodata.gateway.http.request.JfsGetAclStatusRequest;
import org.apache.hadoop.fs.permission.AclStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JfsGetAclStatusCall extends JfsBaseCall {
    private static final Logger LOG = LoggerFactory.getLogger(JfsGetAclStatusCall.class);

    public JfsGetAclStatusCall() {
        request = new JfsGetAclStatusRequest();
        response = new JfsGetAclStatusResponse();
    }

    @Override
    public JfsStatus execute(JfsRequestOptions requestOptions) {
        long startTime = System.currentTimeMillis();
        JfsGetAclStatusRequest req = (JfsGetAclStatusRequest) request;

        String src = req.getSrc();
        String bucket = requestOptions.getBucket();

        LOG.info("GetAclStatus path {} from bucket {}", src, bucket);

        JfsStatus status = super.execute(requestOptions);

        if (status.isOk()) {
            long elapsed = System.currentTimeMillis() - startTime;
            String requestId = response.getRequestId();
            String serverTime = response.getServerTime();

            LOG.info("[RequestId: {}] Successfully GetAclStatus path {} from bucket {} dur {}ms ossServerElapsed {}",
                    requestId, src, bucket, elapsed, serverTime);
        } else {
            String requestId = response.getRequestId();
            LOG.warn("[RequestId: {}] Failed to GetAclStatus path {} from bucket {}, errorCode: {}, errorMessage: {}",
                    requestId, src, bucket, status.getCode(), status.getMessage());
        }

        return status;
    }

    public void setSrc(String src) {
        ((JfsGetAclStatusRequest) request).setSrc(src);
    }

    public AclStatus getResult() {
        return ((JfsGetAclStatusResponse) response).getResult();
    }
}
