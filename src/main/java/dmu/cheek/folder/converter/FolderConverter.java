package dmu.cheek.folder.converter;

import dmu.cheek.folder.model.Folder;
import dmu.cheek.folder.model.FolderDto;
import dmu.cheek.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FolderConverter {

    private final S3Service s3Service;

    public FolderDto convertToDto(Folder folder) {
        if (folder == null)
            return null;

        return FolderDto.builder()
                .folderId(folder.getFolderId())
                .folderName(folder.getFolderName())
                .build();
    }

//    public Folder convertToEntity(FolderDto folderDto) {
//
//    }
}
