
import java.util.List;

/**
 *
 * @author subaochen
 */
public class Client {
    public static void main(String[] args){
        Container container = configureContainer();
        MovieLister lister = (MovieLister)container.getComponent(MovieLister.class);
        List<Movie> movies = lister.moviesDirectedBy("Tim Burton");
        
        for(Movie movie:movies)
            System.out.println(movie.getTitle());
    }
    
    public static Container configureContainer(){
        Container container = new MyContainer();
        Object[] finderParams = new String[]{"movies.txt"};
        container.registerComponent(MovieFinder.class, ColonDelimitedMovieFinder.class, finderParams);
        container.registerComponent(MovieLister.class);
        return container;
    }
}
