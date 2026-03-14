package com.aliyun.jindodata.gateway.call;

import com.aliyun.jindodata.gateway.common.JfsRequestOptions;
import com.aliyun.jindodata.gateway.common.JfsStatus;
import com.aliyun.jindodata.gateway.http.reponse.JfsSetTimesResponse;
import com.aliyun.jindodata.gateway.http.request.JfsSetTimesRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JfsSetTimesCall extends JfsBaseCall {
    private static final Logger LOG = LoggerFactory.getLogger(JfsSetTimesCall.class);

    public JfsSetTimesCall() {
        request = new JfsSetTimesRequest();
        response = new JfsSetTimesResponse();
    }

    @Override
    public JfsStatus execute(JfsRequestOptions requestOptions) {
        long startTime = System.currentTimeMillis();
        JfsSetTimesRequest req = (JfsSetTimesRequest) request;

        String path = req.getPath();
        String bucket = requestOptions.getBucket();

        LOG.info("SetTimes path {} atime={} mtime={} from bucket {}", path, req.getAtime(), req.getMtime(), bucket);

        JfsStatus status = super.execute(requestOptions);

        if (status.isOk()) {
            long elapsed = System.currentTimeMillis() - startTime;
            String requestId = response.getRequestId();
            String serverTime = response.getServerTime();

            LOG.info("[RequestId: {}] Successfully SetTimes path {} atime={} mtime={} from bucket {} dur {}ms ossServerElapsed {}",
                    requestId, path, req.getAtime(), req.getMtime(), bucket, elapsed, serverTime);
        } else {
            String requestId = response.getRequestId();
            LOG.warn("[RequestId: {}] Failed to SetTimes path {} from bucket {}, errorCode: {}, errorMessage: {}",
                    requestId, path, bucket, status.getCode(), status.getMessage());
        }

        return status;
    }

    public void setPath(String path) {
        request.setPath(path);
    }

    public void setAtime(long atime) {
        ((JfsSetTimesRequest) request).setAtime(atime);
    }

    public void setMtime(long mtime) {
        ((JfsSetTimesRequest) request).setMtime(mtime);
    }
}
