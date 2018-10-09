package info.houseofkim.movieproject.model;

public class MovieInfo {
    private int movieId;
    private String movieName;
    private String movieReleaseDate;
    public String image; //String to poster
    private String movieDuration;
    private String movieDescription;
    private double movieRating;
    private double moviePopularity;
    private String movieTrailer1;
    private VideoInfo[] movieTrailer2;
    private String movieReview1;
    private ReviewInfo[] movieReview2;

    public MovieInfo(int id, String name, String releasedate, String image, String description, String duration, double rating, double popularity) {
        this.movieId = id;
        this.movieName = name;
        this.movieReleaseDate = releasedate;
        this.image = image;
        this.movieDescription = description;
        this.movieDuration = duration;
        this.movieRating = rating;
        this.moviePopularity = popularity;

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
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

    public void setMovieId(int id) {
        this.movieId = id;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieRating(Float rating) {
        this.movieRating = rating;
    }

    public double getMovieRating() {
        return movieRating;
    }

    public double getMoviePopularity() {
        return moviePopularity;
    }

    public void setMoviePopularity(double moviePopularity) {
        this.moviePopularity = moviePopularity;
    }

    public VideoInfo[] getMovieTrailer2() {
        return movieTrailer2;
    }

    public void setMovieTrailer2(VideoInfo[] movieTrailer2) {
        this.movieTrailer2 = movieTrailer2;
    }

    public String getMovieReview1() {
        return movieReview1;
    }

    public void setMovieReview1(String movieReview1) {
        this.movieReview1 = movieReview1;
    }

    public ReviewInfo[] getMovieReview2() {
        return movieReview2;
    }

    public void setMoviewReview2(ReviewInfo[] movieReview2) {
        this.movieReview2 = movieReview2;
    }
}
