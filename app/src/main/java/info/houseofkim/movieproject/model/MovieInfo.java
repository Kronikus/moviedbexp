package info.houseofkim.movieproject.model;

public class MovieInfo {
    private String movieName;
    private String movieReleaseDate;
    public int image; //drawable reference id
    private String movieDuration;
    private String movieDescription;
    private String movieTrailer1;
    private String movieTrailer2;
    private String movieTrailer3;
    public MovieInfo(String name,String releasedate,int image,String description, String duration)
    {
        this.movieName = name;
        this.movieReleaseDate= releasedate;
        this.image = image;
        this.movieDescription=description;
        this.movieDuration = duration;
    }

    public String getMovieDescription() {
        return movieDescription;
    }

    public void setMovieDescription(String movieDescription) {
        this.movieDescription = movieDescription;
    }

    public String getMovieDuration() {
        return movieDuration;
    }

    public void setMovieDuration(String movieDuration) {
        this.movieDuration = movieDuration;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getMovieReleaseDate() {
        return movieReleaseDate;
    }

    public void setMovieReleaseDate(String movieReleaseDate) {
        this.movieReleaseDate = movieReleaseDate;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getMovieTrailer1() {
        return movieTrailer1;
    }

    public void setMovieTrailer1(String movieTrailer1) {
        this.movieTrailer1 = movieTrailer1;
    }
}
