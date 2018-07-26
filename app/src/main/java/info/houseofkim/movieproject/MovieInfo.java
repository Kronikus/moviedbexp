package info.houseofkim.movieproject;

public class MovieInfo {
    String movieName;
    String movieReleaseDate;
    int image; //drawable reference id
    String movieDuration;
    String movieDescription;
    String movieTrailer1;
    String movieTrailer2;
    String movieTrailer3;
    public MovieInfo(String name,String releasedate,int image,String description, String duration)
    {
        this.movieName = name;
        this.movieReleaseDate= releasedate;
        this.image = image;
        this.movieDescription=description;
        this.movieDuration = duration;
    }

}
