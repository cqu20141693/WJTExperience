package com.wcc.oss.minio;

import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

@Slf4j
public class MinioTest {
    //创建MinioClient对象
    MinioClient minioClient =
            MinioClient.builder()
                    .endpoint("http://10.82.133.250:9000")
                    .credentials("ga-ea", "ga-ea2021@")
                    .build();
    String name = "scada/202212/20221223160901221170107.png";
    String bucket = "ga-ea";

    @Test
    public void testGetObject() {

        try (GetObjectResponse stream = minioClient.getObject(GetObjectArgs.builder().bucket(bucket).object(name).build());) {

            byte[] bytes = getBytes(stream);
            log.info("read minio object size={}", bytes.length);
        } catch (Exception e) {
            e.printStackTrace();
            log.info(e.getMessage());
        }

    }

    private byte[] getBytes(InputStream inputStream) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            byte[] buf = new byte[1024];
            int length;
            while ((length = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, length);
            }
            return outputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[0];
    }
}
