
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author subaochen
 */
public class ColonDelimitedMovieFinder implements MovieFinder{
    private String movieFile;
    public ColonDelimitedMovieFinder(String movieFile){
        this.movieFile = movieFile;
        
    }

    @Override
    public List<Movie> findAll() {
        List<Movie> movies = new ArrayList<Movie>(0);
        try {
            InputStream is = getClass().getResourceAsStream(movieFile);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String movieLine = br.readLine();
            while(movieLine != null){
                String[] items = movieLine.split(",");
                Movie movie = new Movie(items[0],items[1]);
                movies.add(movie);
                
                movieLine = br.readLine();
            }
            
            br.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        return movies;
    }
    
}
