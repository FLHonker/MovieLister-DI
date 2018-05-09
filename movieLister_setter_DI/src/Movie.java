
/**
 *
 * @author subaochen
 */
class Movie {
    private String director;
    private String title;
    private String description;
    
    public Movie(String director,String title){
        this.director = director;
        this.title = title;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
}
