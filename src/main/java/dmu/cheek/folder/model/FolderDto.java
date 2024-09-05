package dmu.cheek.folder.model;

import lombok.Builder;
import lombok.Getter;

@Getter
public class FolderDto {

    private long folderId;

    private String folderName;

    @Builder @Getter
    public static class Response {
        private long folderId;
        private String folderName;
        private String thumbnailPicture;
    }
}
