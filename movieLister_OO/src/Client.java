import java.util.List;

/**
 *
 * @author Frank
 */
public class Client {
    public static void main(String[] args){
        List<Movie> movies = new MovieLister("movies.txt").moviesDirectedBy("Tim Burton");
        
        for(Movie movie:movies)
            System.out.println(movie.getTitle());
    }
}
