package dmu.cheek.collection.converter;

import dmu.cheek.collection.model.Collection;
import dmu.cheek.collection.model.CollectionDto;
import org.springframework.stereotype.Component;

@Component
public class CollectionConverter {

    public CollectionDto convertToDto(Collection collection) {
        if (collection == null)
            return null;

        return CollectionDto.builder()
                .collectionId(collection.getCollectionId())
                .build();
    }

    public Collection convertToEntity(CollectionDto collectionDto) {
        if (collectionDto == null)
            return null;

        return Collection.naturalFields()
                .collectionId(collectionDto.getCollectionId())
                .build();
    }
}
