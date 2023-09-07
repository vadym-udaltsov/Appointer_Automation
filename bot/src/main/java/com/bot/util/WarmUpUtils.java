package com.bot.util;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.bot.dagger.AwsClientProvider;
import com.bot.dagger.DaoProvider;
import com.bot.dao.IContextDao;
import com.commons.dao.IDepartmentDao;
import com.commons.dao.impl.DynamoDbFactory;
import com.commons.utils.JsonUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

public class WarmUpUtils {

    public static void warmUp() {
        DynamoDbFactory factory = AwsClientProvider.dynamoDbFactory(AwsClientProvider.dynamoDb());
        IDepartmentDao dao = DaoProvider.departmentDao(factory);
        dao.getDepartmentById("test");
        IContextDao contextDao = DaoProvider.contextDao(factory);
        contextDao.getContext(5255252L, "test");
        String updateStr = "{\n" +
                "    \"update_id\": 982746098,\n" +
                "    \"message\": {\n" +
                "        \"message_id\": 4257,\n" +
                "        \"from\": {\n" +
                "            \"id\": 538025182,\n" +
                "            \"first_name\": \"Sergey\",\n" +
                "            \"is_bot\": false,\n" +
                "            \"last_name\": \"Udaltsov\",\n" +
                "            \"username\": \"Sergudal\",\n" +
                "            \"language_code\": \"ru\"\n" +
                "        },\n" +
                "        \"date\": 1690837871,\n" +
                "        \"chat\": {\n" +
                "            \"id\": 538025182,\n" +
                "            \"type\": \"private\",\n" +
                "            \"first_name\": \"Sergey\",\n" +
                "            \"last_name\": \"Udaltsov\",\n" +
                "            \"username\": \"Sergudal\"\n" +
                "        },\n" +
                "        \"text\": \"Выходной\"\n" +
                "    }\n" +
                "}";
        JsonUtils.parseStringToObject(updateStr, Update.class);
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard().build();
        String account = System.getenv("ACCOUNT");
        ListObjectsRequest request = new ListObjectsRequest();
        request.setBucketName("appointer-localization-" + account);
        ObjectListing result = s3Client.listObjects(request);
    }
}
