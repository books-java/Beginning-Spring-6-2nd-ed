package com.bsg6.data.mongodb;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;

import com.bsg6.data.mongodb.repository.ArtistRepository;
import com.bsg6.data.mongodb.repository.SongRepository;
import com.bsg6.data.mongodb.service.MusicService;
import com.bsg6.data.util.WildcardConverter;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
/*
 * with @SpringBootConfiguration.
 * This will scan the current package (and any packages whose names start with
 * com.bsg6.chapter09.jpa) for Spring components – which will catch our
 * MusicService and
 * ArtistRepository and SongRepository interfaces; with @EnableJpaRepositories
 * we’re
 * informing Spring of what kind of services to implement (and where to look for
 * classes that
 * have the @Repository annotation).
 * We also use the @EntityScan annotation to force scanning for entities
 * (classes
 * marked with @Entity) in the current package (and any “subpackages” – i.e.,
 * packages
 * whose names start with this package’s name.) We won’t need this to run our
 * tests – the
 * test annotations will do this for us – but in “real code” we’ll want it.
 * We also create our JPA-compatible WildcardConverter as a component.
 */
@SpringBootConfiguration()
@EnableMongoRepositories
@EntityScan
public class MongoConfiguration {

    @Bean
    WildcardConverter converter() {
        return new WildcardConverter("");
    }

    @Bean
    MusicService musicService(
            ArtistRepository artistRepository,
            SongRepository songRepository,
            WildcardConverter converter) {
        return new MusicService(artistRepository, songRepository,
                converter);
    }
}