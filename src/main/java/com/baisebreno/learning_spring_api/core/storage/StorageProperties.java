package com.baisebreno.learning_spring_api.core.storage;

import com.amazonaws.regions.Regions;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;

@Getter
@Setter
@Configuration
@ConfigurationProperties("algafood.storage")
public class StorageProperties {
    private Local local = new Local();;
    private S3 s3 = new S3();
    private StorageType type = StorageType.LOCAL;

    public enum StorageType{
        LOCAL,
        S3
    }

    @Getter
    @Setter
    public class Local{
        private Path photoDirectory;
    }

    @Getter
    @Setter
    public class S3{

        private String idAccessKey;
        private String SecretAccessKey;
        private Regions region;
        private String bucket;
        private String photoDirectory;

    }

}
