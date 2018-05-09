

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author subaochen
 */
public class MovieLister {
    private MovieFinder finder;

    public MovieFinder getFinder() {
        return finder;
    }

    public void setFinder(MovieFinder finder) {
        this.finder = finder;
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
