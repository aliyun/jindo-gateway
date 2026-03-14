package com.aliyun.jindodata.gateway.call;

import com.aliyun.jindodata.gateway.common.JfsRequestOptions;
import com.aliyun.jindodata.gateway.common.JfsStatus;
import com.aliyun.jindodata.gateway.http.reponse.JfsConcatFileResponse;
import com.aliyun.jindodata.gateway.http.request.JfsConcatFileRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JfsConcatFileCall extends JfsBaseCall {
    private static final Logger LOG = LoggerFactory.getLogger(JfsConcatFileCall.class);

    public JfsConcatFileCall() {
        request = new JfsConcatFileRequest();
        response = new JfsConcatFileResponse();
    }

    @Override
    public JfsStatus execute(JfsRequestOptions requestOptions) {
        long startTime = System.currentTimeMillis();
        JfsConcatFileRequest req = (JfsConcatFileRequest) request;

        String path = req.getPath();
        String[] sources = req.getSources();
        String bucket = requestOptions.getBucket();

        LOG.info("ConcatFile for {} srcsSize {} from bucket {}", path, sources != null ? sources.length : 0, bucket);

        JfsStatus status = super.execute(requestOptions);

        if (status.isOk()) {
            long elapsed = System.currentTimeMillis() - startTime;
            String requestId = response.getRequestId();
            String serverTime = response.getServerTime();

            LOG.info("[RequestId: {}] Successfully ConcatFile {} srcsSize {} from bucket {} dur {}ms ossServerElapsed {}",
                    requestId, path, sources != null ? sources.length : 0, bucket, elapsed, serverTime);
        } else {
            String requestId = response.getRequestId();
            LOG.warn("[RequestId: {}] Failed to ConcatFile {} srcsSize {} from bucket {}, errorCode: {}, errorMessage: {}",
                    requestId, path, sources != null ? sources.length : 0, bucket, status.getCode(), status.getMessage());
        }

        return status;
    }

    public void setPath(String path) {
        request.setPath(path);
    }

    public void setSources(String[] sources) {
        ((JfsConcatFileRequest) request).setSources(sources);
    }

    public boolean getResult() {
        return ((JfsConcatFileResponse) response).getResult();
    }
}
