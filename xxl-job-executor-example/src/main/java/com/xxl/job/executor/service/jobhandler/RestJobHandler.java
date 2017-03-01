package com.xxl.job.executor.service.jobhandler;

import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHander;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created by yjj on 2017/3/1.
 */
@JobHander(value="restPostJobHandler")
@Service
public class RestJobHandler extends IJobHandler {
    private static transient Logger logger = LoggerFactory.getLogger(RestJobHandler.class);

    @Override
    public void execute(String... params) throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://targethost/login");
        CloseableHttpResponse response = httpclient.execute(httpPost);
        try {
            logger.info("{}",response.getStatusLine());
            HttpEntity entity = response.getEntity();
            EntityUtils.consume(entity);
        } finally {
            response.close();
        }
    }
}
