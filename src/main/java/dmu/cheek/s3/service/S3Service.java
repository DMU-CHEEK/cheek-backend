package dmu.cheek.s3.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import dmu.cheek.global.error.ErrorCode;
import dmu.cheek.global.error.exception.BusinessException;
import dmu.cheek.s3.model.S3Dto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3Service {

    private final AmazonS3 amazonS3;
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket-name}")
    private String bucketName;

    public S3Dto saveFile(MultipartFile multipartFile) {
        String originalFileName = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFileName);

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(multipartFile.getSize());
        objectMetadata.setContentType(multipartFile.getContentType());

        try {
            amazonS3.putObject(bucketName, storeFileName, multipartFile.getInputStream(), objectMetadata);
        } catch (IOException | RuntimeException exception) {
            log.error("failed to upload image: {}", exception);
            throw new BusinessException(ErrorCode.UPLOAD_FAILED);
        }

        log.info("image uploaded to AWS S3: {}", originalFileName);

        return S3Dto.builder()
                .storeFileName(storeFileName)
                .resourceUrl(amazonS3Client.getResourceUrl(bucketName, storeFileName))
                .build();
    }

    private String createStoreFileName(String originalFilename) {
        //서버에 저장하는 파일명
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    private String extractExt(String originalFilename) {
        //확장자명 추출
        int pos = originalFilename.lastIndexOf("."); //위치
        return originalFilename.substring(pos + 1);
    }

    public void deleteFile(String storeFileName) {
        amazonS3.deleteObject(bucketName, storeFileName);
    }

    public String getResourceUrl(String storeFileName) {
        return amazonS3Client.getResourceUrl(bucketName, storeFileName);
    }

}
