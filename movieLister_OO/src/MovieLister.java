import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Frank
 */
public class MovieLister {
    MovieFinder finder;
    
    public MovieLister(String movies){
        finder = new ColonDelimitedMovieFinder(movies);
    }
    
    public List<Movie> moviesDirectedBy(String director){
        List movies = new ArrayList<Movie>(0);
        List<Movie> allMovies = finder.findAll();
        for(Movie movie:allMovies)
            if(movie.getDirector().equalsIgnoreCase(director))
                movies.add(movie);
        
        return movies;
    }
}
