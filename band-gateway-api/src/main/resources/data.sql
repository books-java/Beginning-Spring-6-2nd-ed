INSERT INTO ARTIST (NAME)
VALUES ('Threadbare Loaf');
INSERT INTO ARTIST (NAME)
VALUES ('Therapy Zeppelin');
INSERT INTO ARTIST (NAME)
VALUES ('Clancy In Silt');

INSERT INTO SONG (ARTIST_ID, NAME, VOTES)
VALUES ((select id from artist where name like 'Thre%'),
        'Someone Stole the Flour', 4);
INSERT INTO SONG (ARTIST_ID, NAME, VOTES)
VALUES ((select id from artist where name like 'Thre%'),
        'What Happened to Our First CD?', 17);
INSERT INTO SONG (ARTIST_ID, NAME, VOTES)
VALUES ((select id from artist where name like 'The%'),
        'Medium', 4);
INSERT INTO SONG (ARTIST_ID, NAME, VOTES)
VALUES ((select id from artist where name like 'C%'),
        'Igneous', 5);