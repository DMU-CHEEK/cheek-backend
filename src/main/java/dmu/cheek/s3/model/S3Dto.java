package dmu.cheek.s3.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class S3Dto {
    private String storeFileName;
    private String resourceUrl;
}
