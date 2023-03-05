package com.bot;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.spring.SpringBootLambdaContainerHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@Slf4j
public class LambdaApplication {

    public static SpringBootLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler;

    public static void main(String[] args) throws ContainerInitializationException {
        handler = SpringBootLambdaContainerHandler.getAwsProxyHandler(LambdaApplication.class);
    }

//	public static void main(String[] args) throws ContainerInitializationException, IOException {
//		handler = SpringBootLambdaContainerHandler.getAwsProxyHandler(LambdaApplication.class);
//		String reqStr = "{\n" +
//				"    \"body\": null,\n" +
//				"    \"resource\": \"/bot/admin/auth\",\n" +
//				"    \"requestContext\": {\n" +
//				"        \"resourceId\": \"e014x1\",\n" +
//				"        \"apiId\": \"pro8n2j81a\",\n" +
//				"        \"resourcePath\": \"/bot/admin/auth\",\n" +
//				"        \"httpMethod\": \"POST\",\n" +
//				"        \"requestId\": \"cc2fddcd-7032-4adf-a7d9-f04d652d36f9\",\n" +
//				"        \"extendedRequestId\": \"BDlW_Gt1FiAFr1g=\",\n" +
//				"        \"accountId\": \"773974733061\",\n" +
//				"        \"identity\": {\n" +
//				"            \"apiKey\": \"test-invoke-api-key\",\n" +
//				"            \"apiKeyId\": \"test-invoke-api-key-id\",\n" +
//				"            \"userArn\": \"arn:aws:iam::773974733061:user/catamaz_dev\",\n" +
//				"            \"cognitoAuthenticationType\": null,\n" +
//				"            \"caller\": \"AIDA3IND5GECYC4L45LLN\",\n" +
//				"            \"userAgent\": \"aws-internal/3 aws-sdk-java/1.12.407 Linux/5.4.228-141.415.amzn2int.x86_64 OpenJDK_64-Bit_Server_VM/25.362-b10 java/1.8.0_362 vendor/Oracle_Corporation cfg/retry-mode/standard\",\n" +
//				"            \"user\": \"AIDA3IND5GECYC4L45LLN\",\n" +
//				"            \"cognitoIdentityPoolId\": null,\n" +
//				"            \"cognitoIdentityId\": null,\n" +
//				"            \"cognitoAuthenticationProvider\": null,\n" +
//				"            \"sourceIp\": \"test-invoke-source-ip\",\n" +
//				"            \"accountId\": \"773974733061\",\n" +
//				"            \"accessKey\": \"ASIA3IND5GECRM3SEZNH\"\n" +
//				"        },\n" +
//				"        \"authorizer\": null,\n" +
//				"        \"stage\": \"test-invoke-stage\",\n" +
//				"        \"path\": \"/bot/admin/auth\",\n" +
//				"        \"protocol\": \"HTTP/1.1\",\n" +
//				"        \"requestTime\": \"28/Feb/2023:15:22:20 +0000\",\n" +
//				"        \"requestTimeEpoch\": 1677597740433,\n" +
//				"        \"elb\": null\n" +
//				"    },\n" +
//				"    \"multiValueQueryStringParameters\": null,\n" +
//				"    \"queryStringParameters\": null,\n" +
//				"    \"multiValueHeaders\": null,\n" +
//				"    \"headers\": null,\n" +
//				"    \"pathParameters\": null,\n" +
//				"    \"httpMethod\": \"POST\",\n" +
//				"    \"stageVariables\": null,\n" +
//				"    \"path\": \"/bot/admin/auth\",\n" +
//				"    \"isBase64Encoded\": false,\n" +
//				"    \"requestSource\": \"API_GATEWAY\"\n" +
//				"}";
//		ObjectMapper mapper = new ObjectMapper();
//		AwsProxyRequest request = mapper.readValue(reqStr, AwsProxyRequest.class);
//		byte[] bytes = mapper.writeValueAsBytes(request);
//		ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
//		handler.proxyStream(inputStream, new ByteArrayOutputStream(), null);
//		System.out.println("---------------------");
//	}
}
