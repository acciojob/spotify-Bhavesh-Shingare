package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class SpotifyRepository {
    public HashMap<Artist, List<Album>> artistAlbumMap;
    public HashMap<Album, List<Song>> albumSongMap;
    public HashMap<Playlist, List<Song>> playlistSongMap;
    public HashMap<Playlist, List<User>> playlistListenerMap;
    public HashMap<User, Playlist> creatorPlaylistMap;
    public HashMap<User, List<Playlist>> userPlaylistMap;
    public HashMap<Song, List<User>> songLikeMap;

    public List<User> users;
    public List<Song> songs;
    public List<Playlist> playlists;
    public List<Album> albums;
    public List<Artist> artists;

    public SpotifyRepository(){
        //To avoid hitting apis multiple times, initialize all the hashmaps here with some dummy data
        artistAlbumMap = new HashMap<>();
        albumSongMap = new HashMap<>();
        playlistSongMap = new HashMap<>();
        playlistListenerMap = new HashMap<>();
        creatorPlaylistMap = new HashMap<>();
        userPlaylistMap = new HashMap<>();
        songLikeMap = new HashMap<>();

        users = new ArrayList<>();
        songs = new ArrayList<>();
        playlists = new ArrayList<>();
        albums = new ArrayList<>();
        artists = new ArrayList<>();
    }

    public User createUser(String name, String mobile) {
        for(User currUser: users){
            if(currUser.getMobile().equals(mobile)){
                return currUser;
            }
        }
        User newUser= new User(name,mobile);
        users.add(newUser);
        return newUser;
    }

    public Artist createArtist(String name) {
        for(Artist artist: artists){
            if(artist.getName().equals(name))
                return artist;
        }
        Artist newArtist = new Artist(name);
        artists.add(newArtist);
        return newArtist;
    }

    public Album createAlbum(String title, String artistName) {
        Artist artist= createArtist(artistName);
        for(Album album : albums){
            if(album.getTitle().equals(title))
                return  album;
        }

        Album newAlbum = new Album(title);
        albums.add(newAlbum);

        List<Album> albumList = new ArrayList<>();
        if(artistAlbumMap.containsKey(artist)){
            albumList=artistAlbumMap.get(artist);
        }
        albumList.add(newAlbum);
        artistAlbumMap.put(artist,albumList);
        return newAlbum;
    }

    public Song createSong(String title, String albumName, int length) throws Exception{
        boolean isAlbumPresent = false;
        Album album = new Album();
        for(Album currAlbum : albums){
            if(currAlbum.getTitle().equals(albumName)){
                album=currAlbum;
                isAlbumPresent = true;
                break;
            }
        }
        if(isAlbumPresent==false){
            throw new Exception("Album does not exist");
        }
        Song song = new Song(title,length);
        songs.add(song);

        List<Song> songsList= new ArrayList<>();
        if(albumSongMap.containsKey(album)){
            songsList=albumSongMap.get(album);
        }
        songsList.add(song);
        albumSongMap.put(album,songsList);

        return song;

    }

    public Playlist createPlaylistOnLength(String mobile, String title, int length) throws Exception {
        for(Playlist playlist : playlists){
            if(playlist.getTitle().equals(title))
                return  playlist;
        }
        Playlist newPlaylist = new Playlist(title);
        playlists.add(newPlaylist);

        List<Song> list= new ArrayList<>();
        for(Song song : songs){
            if(song.getLength()==length){
                list.add(song);
            }
        }
        playlistSongMap.put(newPlaylist,list);

        User currUser= new User();
        boolean flag = false;
        for(User user: users){
            if(user.getMobile().equals(mobile)){
                currUser=user;
                flag = true;
                break;
            }
        }
        if (flag==false){
            throw new Exception("User does not exist");
        }

        List<User> userslist = new ArrayList<>();
        if(playlistListenerMap.containsKey(newPlaylist)){
            userslist=playlistListenerMap.get(newPlaylist);
        }
        userslist.add(currUser);
        playlistListenerMap.put(newPlaylist,userslist);

        creatorPlaylistMap.put(currUser,newPlaylist);

        List<Playlist>userplaylists = new ArrayList<>();
        if(userPlaylistMap.containsKey(currUser)){
            userplaylists=userPlaylistMap.get(currUser);
        }
        userplaylists.add(newPlaylist);
        userPlaylistMap.put(currUser,userplaylists);
        return newPlaylist;

    }

    public Playlist createPlaylistOnName(String mobile, String title, List<String> songTitles) throws Exception {
        for(Playlist playlist : playlists){
            if(playlist.getTitle().equals(title))
                return  playlist;
        }
        Playlist newPlaylist = new Playlist(title);

        playlists.add(newPlaylist);

        List<Song> list= new ArrayList<>();
        for(Song song : songs){
            if(songTitles.contains(song.getTitle())){
                list.add(song);
            }
        }
        playlistSongMap.put(newPlaylist,list);

        User currUser = new User();
        boolean flag = false;
        for(User user: users){
            if(user.getMobile().equals(mobile)){
                currUser=user;
                flag = true;
                break;
            }
        }
        if (flag==false){
            throw new Exception("User does not exist");
        }

        List<User> userslist = new ArrayList<>();
        if(playlistListenerMap.containsKey(newPlaylist)){
            userslist=playlistListenerMap.get(newPlaylist);
        }
        userslist.add(currUser);
        playlistListenerMap.put(newPlaylist,userslist);

        creatorPlaylistMap.put(currUser,newPlaylist);

        List<Playlist>userplaylists = new ArrayList<>();
        if(userPlaylistMap.containsKey(currUser)){
            userplaylists=userPlaylistMap.get(currUser);
        }
        userplaylists.add(newPlaylist);
        userPlaylistMap.put(currUser,userplaylists);

        return newPlaylist;
    }

    public Playlist findPlaylist(String mobile, String playlistTitle) throws Exception {
        boolean flag1 =false;
        Playlist newPlaylist = new Playlist();
        for(Playlist currplaylist: playlists){
            if(currplaylist.getTitle().equals(playlistTitle)){
                newPlaylist=currplaylist;
                flag1 = true;
                break;
            }
        }
        if (flag1==false){
            throw new Exception("Playlist does not exist");
        }

        User currUser= new User();
        boolean flag2 = false;
        for(User user: users){
            if(user.getMobile().equals(mobile)){
                currUser=user;
                flag2 = true;
                break;
            }
        }
        if (flag2==false){
            throw new Exception("User does not exist");
        }

        List<User> usersList = new ArrayList<>();
        if(playlistListenerMap.containsKey(newPlaylist)){
            usersList=playlistListenerMap.get(newPlaylist);
        }
        if(!usersList.contains(currUser))
            usersList.add(currUser);
        playlistListenerMap.put(newPlaylist,usersList);

        if(creatorPlaylistMap.get(currUser)!=newPlaylist)
            creatorPlaylistMap.put(currUser,newPlaylist);

        List<Playlist>userplaylists = new ArrayList<>();
        if(userPlaylistMap.containsKey(currUser)){
            userplaylists=userPlaylistMap.get(currUser);
        }
        if(!userplaylists.contains(newPlaylist))userplaylists.add(newPlaylist);
        userPlaylistMap.put(currUser,userplaylists);


        return newPlaylist;
    }

    public Song likeSong(String mobile, String songTitle) throws Exception {
        User currUser = new User();
        boolean flag1 = false;
        for(User user: users){
            if(user.getMobile().equals(mobile)){
                currUser=user;
                flag1 = true;
                break;
            }
        }
        if (flag1==false){
            throw new Exception("User does not exist");
        }

        Song song = new Song();
        boolean flag2 = false;

        for(Song currsong : songs){
            if(currsong.getTitle().equals(songTitle)){
                song=currsong;
                flag2 = true;
                break;
            }
        }
        if (flag2==false){
            throw new Exception("Song does not exist");
        }

        List<User> users = new ArrayList<>();
        if(songLikeMap.containsKey(song)){
            users=songLikeMap.get(song);
        }
        if (!users.contains(currUser)){
            users.add(currUser);
            songLikeMap.put(song,users);
            song.setLikes(song.getLikes()+1);

            Album album = new Album();
            for(Album currAlbum : albumSongMap.keySet()){
                List<Song> list = albumSongMap.get(currAlbum);
                if(list.contains(song)){
                    album=currAlbum;
                    break;
                }
            }

            Artist artist = new Artist();
            for(Artist currArtist : artistAlbumMap.keySet()){
                List<Album> list = artistAlbumMap.get(currArtist);
                if(list.contains(album)){
                    artist=currArtist;
                    break;
                }
            }

            artist.setLikes(artist.getLikes()+1);
        }
        return song;
    }

    public String mostPopularArtist() {
        String artistName="";
        int mostLikes = Integer.MIN_VALUE;

        for(Artist art : artists){
            mostLikes= Math.max(mostLikes,art.getLikes());
        }

        for(Artist art : artists){
            if(mostLikes==art.getLikes()){
                artistName=art.getName();
            }
        }

        return artistName;
    }

    public String mostPopularSong() {
        String songName="";
        int mostLikes = Integer.MIN_VALUE;

        for(Song song : songs){
            mostLikes=Math.max(mostLikes,song.getLikes());
        }

        for(Song song : songs){
            if(mostLikes==song.getLikes())
                songName=song.getTitle();
        }

        return songName;
    }
}
