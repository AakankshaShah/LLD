import java.util.*;


class Movie {
    int movieId;
    String movieName;
    String movieRating;
    int movieDuration;

    public Movie(int movieId, String name, String rating, int duration) {
        this.movieId = movieId;
        this.movieName = name;
        this.movieRating = rating;
        this.movieDuration = duration;
    }
}
class Seat {
    int seatNumber;
    boolean isAvailable;

    public Seat(int number) {
        this.seatNumber = number;
        this.isAvailable = true;
    }
}
class Screen {
    String screenName;
    List<Show> shows;

    public Screen(String name) {
        this.screenName = name;
        this.shows = new ArrayList<>();
    }
}
class Show {
    int showId;
    Movie movie;
    int showStartTime; 
    Screen screen;
    List<Seat> seats;

    public Show(int id, Movie movie, int time, Screen screen, int totalSeats) {
        this.showId = id;
        this.movie = movie;
        this.showStartTime = time;
        this.screen = screen;
        this.seats = new ArrayList<>();
        for (int i = 1; i <= totalSeats; i++) {
            this.seats.add(new Seat(i));
        }
    }

    public void displayAvailableSeats() {
        System.out.print("Available seats: ");
        for (Seat seat : seats) {
            if (seat.isAvailable) {
                System.out.print(seat.seatNumber + " ");
            }
        }
        System.out.println();
    }

    public boolean bookSeat(int seatNumber) {
        for (Seat seat : seats) {
            if (seat.seatNumber == seatNumber && seat.isAvailable) {
                seat.isAvailable = false;
                return true;
            }
        }
        return false;
    }
}
class Theatre {
    String name;
    Location location;
    List<Screen> screens;

    public Theatre(String name, Location location) {
        this.name = name;
        this.location = location;
        this.screens = new ArrayList<>();
    }
}
class Location {
    String cityName;
    String pincode;
    List<Theatre> cinemaHalls;

    public Location(String city, String pin) {
        this.cityName = city;
        this.pincode = pin;
        this.cinemaHalls = new ArrayList<>();
    }
}
class User {
    String userName;
    int userId;

    public User(int id, String name) {
        this.userId = id;
        this.userName = name;
    }
}
class Booking {
    User user;
    Show show;
    int seatNumber;

    public Booking(User user, Show show, int seatNumber) {
        this.user = user;
        this.show = show;
        this.seatNumber = seatNumber;
    }

    public void confirmBooking() {
        if (show.bookSeat(seatNumber)) {
            System.out.println("Booking Confirmed for " + user.userName + " - Seat " + seatNumber + " for movie " + show.movie.movieName);
        } else {
            System.out.println("Seat " + seatNumber + " is already booked or invalid.");
        }
    }
}
class MovieController {
    List<Location> locations;

    public MovieController(List<Location> locations) {
        this.locations = locations;
    }

    public List<Movie> getMoviesByCity(String cityName) {
        Set<Integer> movieIds = new HashSet<>();
        List<Movie> moviesInCity = new ArrayList<>();

        for (Location location : locations) {
            if (location.cityName.equalsIgnoreCase(cityName)) {
                for (Theatre theatre : location.cinemaHalls) {
                    for (Screen screen : theatre.screens) {
                        for (Show show : screen.shows) {
                            if (!movieIds.contains(show.movie.movieId)) {
                                movieIds.add(show.movie.movieId);
                                moviesInCity.add(show.movie);
                            }
                        }
                    }
                }
            }
        }

        return moviesInCity;
    }
}

public class BookMyShow {
    public static void main(String[] args) {
        // Setup: Movies, Theatres, Shows
        Location mumbai = new Location("Mumbai", "400001");
        Location delhi = new Location("Delhi", "110001");

        Theatre pvrMumbai = new Theatre("PVR Juhu", mumbai);
        mumbai.cinemaHalls.add(pvrMumbai);

        Theatre inoxDelhi = new Theatre("INOX CP", delhi);
        delhi.cinemaHalls.add(inoxDelhi);

        Movie inception = new Movie(1, "Inception", "PG-13", 148);
        Movie avengers = new Movie(2, "Avengers", "PG-13", 143);

        Screen screen1 = new Screen("Screen 1");
        Screen screen2 = new Screen("Screen 2");

        Show show1 = new Show(101, inception, 1800, screen1, 10);
        Show show2 = new Show(102, avengers, 2000, screen2, 8);

        screen1.shows.add(show1);
        screen2.shows.add(show2);

        pvrMumbai.screens.add(screen1);
        inoxDelhi.screens.add(screen2);

        // Movie Controller
        List<Location> allLocations = Arrays.asList(mumbai, delhi);
        MovieController movieController = new MovieController(allLocations);

        // Query movies in a city
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter city to find movies: ");
        String cityQuery = scanner.nextLine();

        List<Movie> movies = movieController.getMoviesByCity(cityQuery);
        if (movies.isEmpty()) {
            System.out.println("No movies found in " + cityQuery);
        } else {
            System.out.println("Movies currently playing in " + cityQuery + ":");
            for (Movie m : movies) {
                System.out.println("- " + m.movieName + " (" + m.movieRating + ")");
            }
        }

        scanner.close();
    }
}
