package dmu.cheek.file.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class FileService {
    //TODO: S3 저장

    @Value("${file.dir}")
    private String fileDir;

    @Transactional
    public String storeFile(MultipartFile file) throws IOException {
        if (file.isEmpty())
            throw new RuntimeException(); //TODO: exception

        String storeFilename = createStoreFileName(file.getOriginalFilename()); //디비 저장용 파일명
        file.transferTo(new File(getFullPath(storeFilename))); //로컬에 uuid를 파일명으로 저장

        return storeFilename;
    }

    private String getFullPath(String filename) {
        //경로 + 서버용 파일명
        return fileDir + filename;
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
}
