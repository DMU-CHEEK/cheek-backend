package dmu.cheek.collection.model;

import lombok.Getter;

@Getter
public class CollectionDto {

    private long collectionId;

    private String thumbnail_picture;

    public static class Request {
        private long memberId;
        private long categoryId;
        private long storyId;
    }
}
