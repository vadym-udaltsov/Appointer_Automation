package com.bot.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class S3Property {

    private final String dictionaryBucketName;
}
