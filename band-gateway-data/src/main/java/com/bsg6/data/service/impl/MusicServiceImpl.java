package com.bsg6.data.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.bsg6.data.util.Normalizer;


@Component
public class MusicServiceImpl extends AbstractMusicService {
    /*
     * @Autowired
     * 
     * @Qualifier("bar")
     * Normalizer artistNormalizer;
     * 
     * @Autowired
     * 
     * @Qualifier("foo")
     * Normalizer songNormalizer;
     */
    private final Normalizer artistNormalizer;
    private final Normalizer songNormalizer;

    public MusicServiceImpl(@Autowired @Qualifier("bar") Normalizer artistNormalizer,
            @Autowired @Qualifier("foo") Normalizer songNormalizer) {
        this.artistNormalizer = artistNormalizer;
        this.songNormalizer = songNormalizer;
    }

    public Normalizer getArtistNormalizer() {
        return artistNormalizer;
    }

   /*  public void setArtistNormalizer(Normalizer artistNormalizer) {
        this.artistNormalizer = artistNormalizer;
    } */

    public Normalizer getSongNormalizer() {
        return songNormalizer;
    }

   /*  public void setSongNormalizer(Normalizer songNormalizer) {
        this.songNormalizer = songNormalizer;
    } */

    @Override
    protected String transformArtist(String input) {
        return artistNormalizer.transform(input);
    }

    @Override
    protected String transformSong(String input) {
        return songNormalizer.transform(input);
    }
}
