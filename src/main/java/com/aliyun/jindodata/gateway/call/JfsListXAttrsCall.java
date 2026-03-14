package com.aliyun.jindodata.gateway.call;

import com.aliyun.jindodata.gateway.common.JfsRequestOptions;
import com.aliyun.jindodata.gateway.common.JfsStatus;
import com.aliyun.jindodata.gateway.http.reponse.JfsListXAttrsResponse;
import com.aliyun.jindodata.gateway.http.request.JfsListXAttrsRequest;
import org.apache.hadoop.fs.XAttr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class JfsListXAttrsCall extends JfsBaseCall {
    private static final Logger LOG = LoggerFactory.getLogger(JfsListXAttrsCall.class);

    public JfsListXAttrsCall() {
        request = new JfsListXAttrsRequest();
        response = new JfsListXAttrsResponse();
    }

    @Override
    public JfsStatus execute(JfsRequestOptions requestOptions) {
        long startTime = System.currentTimeMillis();
        JfsListXAttrsRequest req = (JfsListXAttrsRequest) request;

        String src = req.getSrc();
        String bucket = requestOptions.getBucket();

        LOG.info("ListXAttrs path {} from bucket {}", src, bucket);

        JfsStatus status = super.execute(requestOptions);

        if (status.isOk()) {
            long elapsed = System.currentTimeMillis() - startTime;
            String requestId = response.getRequestId();
            String serverTime = response.getServerTime();

            LOG.info("[RequestId: {}] Successfully ListXAttrs path {} from bucket {} dur {}ms ossServerElapsed {}",
                    requestId, src, bucket, elapsed, serverTime);
        } else {
            String requestId = response.getRequestId();
            LOG.warn("[RequestId: {}] Failed to ListXAttrs path {} from bucket {}, errorCode: {}, errorMessage: {}",
                    requestId, src, bucket, status.getCode(), status.getMessage());
        }

        return status;
    }

    public void setSrc(String src) {
        ((JfsListXAttrsRequest) request).setSrc(src);
    }

    public List<XAttr> getResult() {
        return ((JfsListXAttrsResponse) response).getXAttrs();
    }
}
