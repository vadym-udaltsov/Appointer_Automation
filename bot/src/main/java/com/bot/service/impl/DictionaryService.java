package com.bot.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.bot.model.Language;
import com.bot.model.S3Property;
import com.bot.service.IDictionaryService;
import com.commons.model.Department;
import com.commons.utils.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class DictionaryService implements IDictionaryService {

    private final AmazonS3 s3Client;
    private final S3Property s3Property;

    @Override
    public List<String> getDictionaryFileKeys(Department department) {
        ListObjectsRequest request = new ListObjectsRequest();
        request.setBucketName(s3Property.getDictionaryBucketName());
        request.setPrefix(department.getType().getTitle());

        ObjectListing result = s3Client.listObjects(request);

        List<S3ObjectSummary> summaries = result.getObjectSummaries();
        return summaries.stream().map(S3ObjectSummary::getKey).collect(Collectors.toList());
    }

    @Override
    public Map<String, String> getDictionary(Language language, Department department) {
        if (Language.US == language) {
            return new HashMap<>();
        }
        String dictKey = String.format(language.getLocalizationFilePath(), department.getType().getTitle());
        GetObjectRequest request = new GetObjectRequest(s3Property.getDictionaryBucketName(), dictKey);
        S3Object response = s3Client.getObject(request);
        S3ObjectInputStream objectContent = response.getObjectContent();
        return JsonUtils.parseInputStreamToObject(objectContent, new TypeReference<>() {
        });
    }
}
