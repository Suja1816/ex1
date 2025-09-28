// 1. BEHAVIORAL PATTERNS
//Observer Pattern Example: Weather Station 
import java.util.*;
interface WeatherSubject {
    void addObserver(WeatherObserver observer);
    void removeObserver(WeatherObserver observer);
    void notifyObservers(String weather);
}

class WeatherStation implements WeatherSubject {
    private final List<WeatherObserver> observers = new ArrayList<>();
    private String weather;
    public void setWeather(String weather) {
        this.weather = weather;
        notifyObservers(weather);
    }
    @Override
    public void addObserver(WeatherObserver observer) {
        observers.add(observer);
    }
    @Override
    public void removeObserver(WeatherObserver observer) {
        observers.remove(observer);
    }
    @Override
    public void notifyObservers(String weather) {
        for (WeatherObserver o : observers) {
            o.update(weather);
        }
    }
}
interface WeatherObserver {
    void update(String weather);
}
class PhoneDisplay implements WeatherObserver {
    private final String owner;
    public PhoneDisplay(String owner) { this.owner = owner; }
    @Override
    public void update(String weather) {
        System.out.println(owner + "'s phone: Weather updated to " + weather);
    }
}
//Strategy Pattern Example: Payment System 
interface PaymentStrategy {
    void pay(double amount);
}
class CreditCardPayment implements PaymentStrategy {
    @Override
    public void pay(double amount) {
        System.out.println("Paid $" + amount + " with Credit Card.");
    }
}
class PaypalPayment implements PaymentStrategy {
    @Override
    public void pay(double amount) {
        System.out.println("Paid $" + amount + " using PayPal.");
    }
}
class PaymentContext {
    private PaymentStrategy strategy;
    public void setStrategy(PaymentStrategy strategy) {
        this.strategy = strategy;
    }
    public void executePayment(double amount) {
        if (strategy != null) strategy.pay(amount);
        else System.out.println("No payment method selected.");
    }
}

// 2. CREATIONAL PATTERNS
//  Singleton Pattern Example: Logger 
class Logger {
    private static Logger instance;
    private Logger() {}
    public static synchronized Logger getInstance() {
        if (instance == null) instance = new Logger();
        return instance;
    }
    public void log(String msg) {
        System.out.println("[LOG]: " + msg);
    }
}

//  Factory Pattern Example: Shape Factory 
interface Shape {
    void draw();
}
class Circle implements Shape {
    @Override
    public void draw() { System.out.println("Drawing a Circle."); }
}
class Square implements Shape {
    @Override
    public void draw() { System.out.println("Drawing a Square."); }
}
class ShapeFactory {
    public static Shape getShape(String type) {
        switch (type.toLowerCase()) {
            case "circle": return new Circle();
            case "square": return new Square();
            default: throw new IllegalArgumentException("Unknown shape " + type);
        }
    }
}

// 3. STRUCTURAL PATTERNS
// Adapter Pattern Example: Media Player 
interface MediaPlayer {
    void play(String filename);
}
class MP3Player implements MediaPlayer {
    @Override
    public void play(String filename) {
        System.out.println("Playing MP3 file: " + filename);
    }
}
class VLCPlayer {
    public void playVLC(String filename) {
        System.out.println("Playing VLC file: " + filename);
    }
}
class MediaAdapter implements MediaPlayer {
    private final VLCPlayer vlcPlayer;
    public MediaAdapter(VLCPlayer vlcPlayer) { this.vlcPlayer = vlcPlayer; }
    @Override
    public void play(String filename) {
        vlcPlayer.playVLC(filename);
    }
}

// Decorator Pattern Example: Coffee Shop 
interface Coffee {
    String getDescription();
    double getCost();
}
class BasicCoffee implements Coffee {
    @Override
    public String getDescription() { return "Basic Coffee"; }
    @Override
    public double getCost() { return 2.0; }
}
abstract class CoffeeDecorator implements Coffee {
    protected Coffee decoratedCoffee;
    public CoffeeDecorator(Coffee coffee) { this.decoratedCoffee = coffee; }
}
class MilkDecorator extends CoffeeDecorator {
    public MilkDecorator(Coffee coffee) { super(coffee); }
    @Override
    public String getDescription() { return decoratedCoffee.getDescription() + ", Milk"; }
    @Override
    public double getCost() { return decoratedCoffee.getCost() + 0.5; }
}
class SugarDecorator extends CoffeeDecorator {
    public SugarDecorator(Coffee coffee) { super(coffee); }
    @Override
    public String getDescription() { return decoratedCoffee.getDescription() + ", Sugar"; }
    @Override
    public double getCost() { return decoratedCoffee.getCost() + 0.2; }
}

// MAIN DEMO
public class DesignPatternDemo {
    public static void main(String[] args) {
        // 1. Behavioral - Observer
        WeatherStation station = new WeatherStation();
        station.addObserver(new PhoneDisplay("Alice"));
        station.addObserver(new PhoneDisplay("Bob"));
        station.setWeather("Sunny");
        station.setWeather("Rainy");

        // 2. Behavioral - Strategy
        PaymentContext context = new PaymentContext();
        context.setStrategy(new CreditCardPayment());
        context.executePayment(100);
        context.setStrategy(new PaypalPayment());
        context.executePayment(50);

        // 3. Creational - Singleton
        Logger log1 = Logger.getInstance();
        Logger log2 = Logger.getInstance();
        log1.log("Singleton logger works.");
        System.out.println("Logger objects same? " + (log1 == log2));

        // 4. Creational - Factory
        Shape circle = ShapeFactory.getShape("circle");
        circle.draw();
        Shape square = ShapeFactory.getShape("square");
        square.draw();

        // 5. Structural - Adapter
        MediaPlayer mp3 = new MP3Player();
        mp3.play("song.mp3");
        MediaPlayer vlcAdapter = new MediaAdapter(new VLCPlayer());
        vlcAdapter.play("movie.vlc");

        // 6. Structural - Decorator
        Coffee coffee = new BasicCoffee();
        coffee = new MilkDecorator(coffee);
        coffee = new SugarDecorator(coffee);
        System.out.println(coffee.getDescription() + " $" + coffee.getCost());
    }
}

